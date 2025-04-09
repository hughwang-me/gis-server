package com.uwjx.gisserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.*;

/**
 * @author wanghuan
 * @email 18501667737@163.com
 * @date 2025/4/8 09:56
 */
@Slf4j
@RestController
@RequestMapping("/tiles")
public class TileController {

    @Value("${sqlite.mbtiles_path}")
    private String dbPath;
    @Value("${sqlite.png_path}")
    private String pngPath;

    private Connection connection;

    @PostConstruct
    public void init() throws SQLException {
        log.warn("SQLite path: {}", dbPath);
        log.warn("pngPath path: {}", pngPath);
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }


    @GetMapping("/{z}/{x}/{y}.png")
    public ResponseEntity<byte[]> getTile(
            @PathVariable int z,
            @PathVariable int x,
            @PathVariable int y) throws SQLException, IOException {

        // mbtiles 中 tile_row 是“倒 Y”方向（TMS 转 XYZ）
        int tmsY = (1 << z) - 1 - y;

        PreparedStatement stmt = connection.prepareStatement(
                "SELECT tile_data FROM tiles WHERE zoom_level = ? AND tile_column = ? AND tile_row = ?"
        );
        stmt.setInt(1, z);
        stmt.setInt(2, x);
        stmt.setInt(3, tmsY);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            byte[] imageBytes = rs.getBytes("tile_data");
            return ResponseEntity.ok()
                    .header("Content-Type", "image/png")
                    .body(imageBytes);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("file/{z}/{x}/{y}.png")
    public ResponseEntity<Resource> getLocalTile(
            @PathVariable int z,
            @PathVariable int x,
            @PathVariable int y) throws IOException {

        String path = String.format("%s/%d/%d/%d.png", pngPath, z, x, y);
        FileSystemResource resource = new FileSystemResource(path);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .body(resource);
    }
}
