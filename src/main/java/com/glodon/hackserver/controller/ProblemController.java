package com.glodon.hackserver.controller;

import com.aliyun.oss.OSSClient;
import com.glodon.hackserver.bean.*;
import com.glodon.hackserver.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class ProblemController {
    private final static CopyOnWriteArrayList<Problem> problemList = new CopyOnWriteArrayList<>();

    static {
//        problemList.add(new Problem(IdGenerator.generateId(), "井盖移动", "在XX路和XX路之间井盖缺失，严重危及到路人的安全，请尽快处理", "http://duxiaoqier-src.oss-cn-shanghai.aliyuncs.com/image/logo-30_dbc5.png", true, new Reporter("张三", 18666668888L, "巡检员"), new Position(403550.7930246513, 298377.0682940586, -2.5515136246544574), new Gps(116.27, 40.7), new Date()));
//        problemList.add(new Problem(IdGenerator.generateId(), "水管爆裂", "在XX路和XX路之间水管爆裂，严重危及到车辆的安全，请尽快处理", "http://duxiaoqier-src.oss-cn-shanghai.aliyuncs.com/image/logo-30_dbc5.png", true, new Reporter("李四", 18666668887L, "巡检员"), new Position(396196.27366495, 300404.6208116605, 10.012972171671652), new Gps(116.27, 40.7), new Date()));

        problemList.add(new Problem(IdGenerator.generateId(), "垃圾清理", "路东侧暴露垃圾", "http://duxiaoqier-src.oss-cn-shanghai.aliyuncs.com/image/trash.jpg", true, new Reporter("陆剑伟", 18666668886L, "巡检员"), new Position(328920.2374613888, 411983.5867461097, 190.0213001056998), new Gps(121.590575, 31.248656), new Date()));
//        problemList.add(new Problem(IdGenerator.generateId(), "隔离带破坏", "东侧非机动道和隔离带圆头损坏", "http://duxiaoqier-src.oss-cn-shanghai.aliyuncs.com/image/destroy.jpg", false, new Reporter("王五", 18666668886L, "巡检员"), new Position(66320.04584861035, 379580.09231413313, -23.84736155436842), new Gps(116.27, 40.7), new Date()));
        problemList.add(new Problem(IdGenerator.generateId(), "道路下沉", "道路路口黑色下沉凹下8平方左右", "http://duxiaoqier-src.oss-cn-shanghai.aliyuncs.com/image/sink.jpg", false, new Reporter("杜朋", 18666668887L, "巡检员"), new Position(343940.19053073204, 392891.3667175226, 10.265014624082225), new Gps(121.590086, 31.248348), new Date()));
        problemList.add(new Problem(IdGenerator.generateId(), "树枝挡路", "可乐厂门口树枝拉断，要用车拉", "http://duxiaoqier-src.oss-cn-shanghai.aliyuncs.com/image/tree.jpg", false, new Reporter("吴佳云", 18666668888L, "巡检员"), new Position(411785.168556242, 291222.0031493814, 5359.254359440693), new Gps(121.589381, 31.248004), new Date()));
        problemList.add(new Problem(IdGenerator.generateId(), "行道树枯死", "行道的树已经枯死了，需要重新换植物", "http://duxiaoqier-src.oss-cn-shanghai.aliyuncs.com/image/tree-die.jpg", false, new Reporter("蔡科", 18666668889L, "巡检员"), new Position(407328.7892146755, 272699.96249332884, 5662.682122310292), new Gps(121.589116, 31.247931), new Date()));
        problemList.add(new Problem(IdGenerator.generateId(), "绞车疏通污水管", "绞车疏通（机械）-小型-疏通电力管", "http://duxiaoqier-src.oss-cn-shanghai.aliyuncs.com/image/sewage.jpg", false, new Reporter("陆剑伟", 18666668886L, "巡检员"), new Position(429808.0925406682,257495.68138937905, -2673.311247109267), new Gps(121.585424, 31.246295), new Date()));

    }

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private Path uploadLocation = Paths.get("src\\main\\resources\\static\\upload");
    // 上传到生产环境static对应bucket里面，相关配置只在这里用一次
    private static String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    private static String accessKeyId = "5nGlEwOIzrwCVaDZ";
    private static String accessKeySecret = "nWUdEsCxEV2Eu9mulPu6MbTLmG7F9M";
    private static String bucketName = "bf-prod-staticfile";
    private static String keyPrefix = "hack/";
    private static String downloadUrl = "http://%s.oss-cn-beijing.aliyuncs.com/%s";
    private OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

    @GetMapping("problem/{problemId}")
    @ResponseBody
    GeneralResponse<Problem> get(@PathVariable("problemId") Long problemId) {
        Optional<Problem> optional = problemList.stream()
                .filter(p -> p.getId().equals(problemId))
                .findFirst();

        return optional.map(GeneralResponse::new).orElseGet(GeneralResponse::new);
    }

    @GetMapping("problems")
    @ResponseBody
    GeneralResponse<List<Problem>> getProblems() {
        return new GeneralResponse<>(problemList);
    }

    @PatchMapping("problem")
    @ResponseBody
    GeneralResponse<List<Problem>> getList() {
        return new GeneralResponse<>(problemList);
    }

    @PostMapping("problem")
    @ResponseBody
    GeneralResponse<Problem> add(@RequestBody Problem problem) {
        Assert.notNull(problem, "problem must not null");
        Assert.hasText(problem.getName(), "problem name must not null");

        problem.setId(IdGenerator.generateId());
        problem.setSolved(false);

        problemList.add(problem);

        messagingTemplate.convertAndSend("/problems", problemList);
        return new GeneralResponse<>(problem);
    }

    @PostMapping("problem/image")
    @ResponseBody
    GeneralResponse<String> addImage(@RequestParam("image") MultipartFile imageFile) throws IOException {
        String url = uploadImage(imageFile);
        return new GeneralResponse<>(url);
    }

    @PostMapping("problem/form")
    @ResponseBody
    GeneralResponse<Problem> addProblem(@RequestParam("problemTitle") String problemTitle,
                                        @RequestParam("problemContent") String problemContent,
                                        @RequestParam("reporterName") String reporterName,
                                        @RequestParam("phoneNum") Long phoneNum,
                                        @RequestParam("role") String role,
                                        @RequestParam("jd") Double jd,
                                        @RequestParam("wd") Double wd,
                                        @RequestParam("positionX") Double positionX,
                                        @RequestParam("positionY") Double positionY,
                                        @RequestParam("positionZ") Double positionZ,
                                        @RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException {
        Assert.hasText(problemTitle, "problem name must has text");

        Problem problem = new Problem();
        problem.setId(IdGenerator.generateId());
        problem.setName(problemTitle);
        problem.setContent(problemContent);
        problem.setImageUrl(uploadImage(imageFile));
        problem.setReporter(new Reporter(reporterName, phoneNum, role));
        problem.setGps(new Gps(jd, wd));
        problem.setPosition(new Position(positionX, positionY, positionZ));
        problem.setSolved(false);

        problemList.add(problem);
        messagingTemplate.convertAndSend("/problems", problemList);
        return new GeneralResponse<>(problem);
    }

    /**
     * 上传图片
     *
     * @param imageFile
     * @return
     * @throws IOException
     */
    private String uploadImage(@RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException {
        String imageName = imageFile.getOriginalFilename();
        Files.copy(imageFile.getInputStream(), uploadLocation.resolve(imageName),
                StandardCopyOption.REPLACE_EXISTING);

        String ossKey = keyPrefix + IdGenerator.generateId() + imageFile.getOriginalFilename();
        ossClient.putObject(bucketName, ossKey, uploadLocation.resolve(imageName).toFile());
        String url = String.format(downloadUrl, bucketName, ossKey);
        uploadLocation.resolve(imageName).toFile().delete();
        return url;
    }

    @PutMapping("problem")
    @ResponseBody
    GeneralResponse<Problem> update(@RequestBody Problem problem) {
        Assert.notNull(problem, "problem must not null");
        Assert.notNull(problem.getId(), "problem id must not null");

        Optional<Problem> optional = problemList.stream()
                .filter(p -> p.getId().equals(problem.getId()))
                .findFirst();

        if (optional.isPresent()) {
            Problem oldProblem = optional.get();
            oldProblem.setSolved(problem.getSolved());
            messagingTemplate.convertAndSend("/problems", problemList);
            return new GeneralResponse<>(problem);
        } else {
            throw new RuntimeException("problem not exist!");
        }
    }

    @DeleteMapping("problem/{problemId}")
    @ResponseBody
    GeneralResponse delete(@PathVariable("problemId") Long problemId) {
        problemList.removeIf(p -> p.getId().equals(problemId));

        messagingTemplate.convertAndSend("/problems", problemList);
        return new GeneralResponse();
    }
}
