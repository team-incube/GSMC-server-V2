package team.incube.gsmc.v2.domain.sheet.domain;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class InMemoryMultipartFile implements MultipartFile {
    private final String name, filename, type;
    private final byte[] data;

    public InMemoryMultipartFile(String nm, String fn, String tp, byte[] dt) {
        this.name = nm;
        this.filename = fn;
        this.type = tp;
        this.data = dt;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return filename;
    }

    @Override
    public String getContentType() {
        return type;
    }

    @Override
    public boolean isEmpty() {
        return data.length == 0;
    }

    @Override
    public long getSize() {
        return data.length;
    }

    @Override
    public byte[] getBytes() {
        return data.clone();
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(data);
    }

    @Override
    public void transferTo(Path dest) throws IOException {
        Files.write(dest, data);
    }

    @Override
    public void transferTo(File dest) throws IOException {
        try (FileOutputStream os = new FileOutputStream(dest)) {
            os.write(data);
        }
    }
}