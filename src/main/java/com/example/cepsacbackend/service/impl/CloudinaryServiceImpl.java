package com.example.cepsacbackend.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.cepsacbackend.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> upload(MultipartFile file, String folder) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", folder,
                        "resource_type", "auto"
                ));
    }

    @Override
    public void delete(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    @Override
    public String extractPublicId(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        // link:https://res.cloudinary.com/{cloud_name}/image/upload/{version}/{public_id}.{format}
        String[] parts = url.split("/upload/");
        if (parts.length > 1) {
            String pathWithVersion = parts[1];
            //limpiar si existe
            String path = pathWithVersion.replaceFirst("v\\d+/", "");
            int lastDot = path.lastIndexOf('.');
            if (lastDot > 0) {
                return path.substring(0, lastDot);
            }
            return path;
        }
        return null;
    }
}
