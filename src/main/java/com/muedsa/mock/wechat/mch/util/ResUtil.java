package com.muedsa.mock.wechat.mch.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class ResUtil {

    private static final PathMatchingResourcePatternResolver RESOLVER = new PathMatchingResourcePatternResolver();
    private static final String FILE_RESOURCE_ROOT_PATH = new ClassPathResource(new File(".").getAbsolutePath()).getPath() + "/";

    public static void findResourceInputStreams(String relativePath, Map<String, InputStream> foundMap) throws IOException {
        Map<String, InputStream> map = new HashMap<>();
        findFileResourceInputStreams(relativePath, map);
        if(map.isEmpty()){
            findClassPathResourceInputStreams(relativePath, map);
        }
        foundMap.putAll(map);
    }

    public static void findFileResourceInputStreams(String relativePath, Map<String, InputStream> foundMap) throws IOException {
        Resource[] resources = RESOLVER.getResources(ResourceUtils.FILE_URL_PREFIX + StringUtils.applyRelativePath(FILE_RESOURCE_ROOT_PATH, relativePath));
        for (Resource resource : resources) {
            if (resource.exists() && resource.isFile() && resource.isReadable()) {
                foundMap.put(resource.getFilename(), resource.getInputStream());
            }
        }
    }

    public static void findClassPathResourceInputStreams(String relativePath, Map<String, InputStream> foundMap) throws IOException {
        Resource[] resources = RESOLVER.getResources(ResourceUtils.CLASSPATH_URL_PREFIX + relativePath);
        for (Resource resource : resources) {
            if (resource.exists() && resource.isReadable()) {
                foundMap.put(resource.getFilename(), resource.getInputStream());
            }
        }
    }

    public static InputStream getResourceInputStream(String relativePath) throws IOException {
        InputStream inputStream = getFileInputStream(relativePath);
        if(Objects.isNull(inputStream)) inputStream = getClassPathFileInputStream(relativePath);
        return inputStream;
    }

    public static InputStream getFileInputStream(String relativePath) throws IOException {
        Resource fileResource = RESOLVER.getResource(ResourceUtils.FILE_URL_PREFIX + StringUtils.applyRelativePath(FILE_RESOURCE_ROOT_PATH, relativePath));
        InputStream inputStream = null;
        if(fileResource.exists() && fileResource.isFile() && fileResource.isReadable()) {
            inputStream = fileResource.getInputStream();
        }
        return inputStream;
    }

    public static InputStream getClassPathFileInputStream(String relativePath) throws IOException {
        Resource resource = RESOLVER.getResource(ResourceUtils.CLASSPATH_URL_PREFIX + relativePath);
        InputStream inputStream = null;
        if (resource.exists() && resource.isReadable()) {
            inputStream = resource.getInputStream();
        }
        return inputStream;
    }
}
