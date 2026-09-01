package co.yixiang.yshop.module.infra.api.file;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import co.yixiang.yshop.module.infra.framework.file.core.client.FileClient;
import co.yixiang.yshop.module.infra.service.file.FileConfigService;
import co.yixiang.yshop.module.infra.service.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import java.io.InputStream;

/**
 * 文件 API 实现类
 *
 * @author yshop
 */
@Service
@Validated
@Slf4j
public class FileApiImpl implements FileApi {

    @Resource
    private FileService fileService;
    @Resource
    private FileConfigService fileConfigService;

    @Override
    public String createFile(String name, String path, byte[] content) {
        return fileService.createFile(name, path, content);
    }

    @Override
    public InputStream getFile(String path)  {
        System.out.println("getFile path:"+path);
        String path2 = StrUtil.subAfter(path, "/get/", false);
        FileClient fileClient = fileConfigService.getMasterFileClient();
        byte[] content = new byte[0];
        try {
            content = fileClient.getContent(URLUtil.decode(path2));
            System.out.println("getFile content:"+content);
        } catch (Exception e) {
            log.error("获取文件流失败：{}",e.getMessage());
        }
        //byte转换为文件流
        return IoUtil.toStream(content);

    }

}
