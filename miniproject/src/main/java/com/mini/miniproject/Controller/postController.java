package com.mini.miniproject.Controller;


import com.mini.miniproject.dto.*;
import com.mini.miniproject.model.Good;
import com.mini.miniproject.model.Post;
import com.mini.miniproject.model.User;
import com.mini.miniproject.model.Image;
import com.mini.miniproject.repository.GoodRepository;
import com.mini.miniproject.repository.ImageRepository;
import com.mini.miniproject.repository.PostRepository;
import com.mini.miniproject.repository.UserRepository;
import com.mini.miniproject.service.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@EnableJpaAuditing
@RestController
public class postController {
    private final S3Uploader s3Uploader;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private GoodRepository goodRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepository imageRepository;

    public postController(S3Uploader s3Uploader) {
        this.s3Uploader = s3Uploader;
    }


    @GetMapping("/api/posts/{page}")
    public List<getPostDto> postDtoList(@PathVariable("page") Integer pageid){
        List<Post> posts=postRepository.findAll();

        List<getPostDto> getPostDtos=new ArrayList<>();
        for(Post p:posts){
            getPostDto tmp=new getPostDto();
            tmp.setPostId(p.getPostid());
            tmp.setContent(p.getContent());
            tmp.setTitle(p.getTitle());
            tmp.setCreatedAt(p.getCreatedAt());
            tmp.setStar(p.getStar());
            List<Image> a=imageRepository.findAllByPostid(p.getPostid());
//            Long num= Long.valueOf(a.size());
            String[] myar=new String[a.size()];
            for(int i=0;i<a.size();i++)
                myar[i]=a.get(i).getImageSrc();

            tmp.setImageSrc(myar);
//            tmp.setImageSrc(p.getImageSrc());

            List<Good> goods=goodRepository.findAllByPostid(p.getPostid());
            int gs=goods.size();
            tmp.setGood(gs);
            getPostDtos.add(tmp);

        }

        Collections.reverse(getPostDtos);
        List<getPostDto> resultt=new ArrayList<>();
        for(int i=pageid*10-10;i<pageid*10;i++){
            try{
            resultt.add(getPostDtos.get(i));}
            catch(Exception e){return resultt;}

        }
        return resultt;
    }

    @PostMapping("/api/posts")
    public ResponseDto writepost(postDto postDto,@RequestParam("images") MultipartFile[] multipartFile)throws IOException {
        Post post=new Post();
        post.setContent(postDto.getContent());
//        post.setImageSrc(postDto.getImageSrc());
        post.setStar(postDto.getStar());
        post.setTitle(postDto.getTitle());
        Optional<User> user1=userRepository.findUserByUsername(postDto.getUsername());
        User user=user1.get();
        post.setUser(user);
        int leng= multipartFile.length;
//        System.out.println(leng);
//        List<String> urls=new ArrayList<>();

        long imagepid=postRepository.save(post).getPostid();
//        long imag=postRepository.save(post).getPostid();
//        System.out.println(imagepid);

        for(int i=0;i<leng;i++) {
            Image image=new Image();
            image.setImageSrc(s3Uploader.upload(multipartFile[i], "static"));
            image.setPostid(imagepid);
            imageRepository.save(image);

        }
        post.setPostid(imagepid);
        ResponseDto responseDto=new ResponseDto();
        responseDto.setResult(true);
        responseDto.setErr_msg("");




        return responseDto;

    }
    @PutMapping("/api/posts/{postId}")
    public void modifypost(@PathVariable("postId") Integer pid,@RequestParam("images") MultipartFile[] multipartFile, modifyDto modifyDto)throws IOException{
        Post post=new Post();
        post.setPostid(modifyDto.getPostid());
        post.setContent(modifyDto.getContent());
        post.setTitle(modifyDto.getTitle());
//        post.setImageSrc(modifyDto.getImageSrc());
        post.setStar(modifyDto.getStar());
        Optional<User> user1=userRepository.findUserByUsername(modifyDto.getUsername());
        User user=user1.get();
        post.setUser(user);
        Optional<Post> post1=postRepository.findById(modifyDto.getPostid());
        Post post2=post1.get();
        post.setCreatedAt(post2.getCreatedAt());
        postRepository.save(post);
        imageRepository.deleteAllByPostid(modifyDto.getPostid());
        int leng= multipartFile.length;;
        for(int i=0;i<leng;i++) {
            Image image=new Image();
            image.setImageSrc(s3Uploader.upload(multipartFile[i], "static"));
            image.setPostid(modifyDto.getPostid());
            imageRepository.save(image);

        }


    }
    @DeleteMapping("/api/posts/{postId}")
    public ResponseDto deletepost(@PathVariable("postId") long pid){
        ResponseDto responseDto=new ResponseDto();
        postRepository.deleteById(pid);
        imageRepository.deleteAllByPostid(pid);
        responseDto.setErr_msg("");
        responseDto.setResult(true);
        return responseDto;
    }

    @GetMapping("/api/posts/detail/{postid}/{username}")
    public detailDto detailpost(@PathVariable("postid") long pid,@PathVariable("username") String name){
        detailDto result= new detailDto();
        Optional<Post> post=postRepository.findById(pid);
        Post post1=post.get();
        result.setPostId(post1.getPostid());
        result.setTitle(post1.getTitle());
        result.setContent(post1.getContent());
        result.setCreatedAt(post1.getCreatedAt());
        result.setStar(post1.getStar());
        List<Good> goods=goodRepository.findAllByPostid(post1.getPostid());
        int gs=goods.size();
        result.setGood(gs);
        List<Image> a=imageRepository.findAllByPostid(post1.getPostid());
//            Long num= Long.valueOf(a.size());
        String[] myar=new String[a.size()];
        for(int i=0;i<a.size();i++)
            myar[i]=a.get(i).getImageSrc();

        result.setImageSrc(myar);
        result.setHeart(true);
        try{
            List<Good> ggs=goodRepository.findAllByPostidAndUsername(post1.getPostid(),name);
            if(ggs.size()==0)
                result.setHeart(false);
        }
        catch(Exception e){
            result.setHeart(false);
        }

        return result;


    }




}
