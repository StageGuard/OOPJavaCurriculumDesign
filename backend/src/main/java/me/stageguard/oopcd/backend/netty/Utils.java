package me.stageguard.oopcd.backend.netty;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static HashMap<String, String> decodeQueryOpinions(String path) {
        if (path.contains("?")) {
            var raw = path.split("\\?")[1];
            if (!raw.contains("&")) {
                var single = raw.split("=");
                return new HashMap<>(Map.of(
                        URLDecoder.decode(single[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(single[1], StandardCharsets.UTF_8)
                ));
            } else {
                var multi = raw.split("&");
                HashMap<String, String> opinions = new HashMap<>();
                for (var element : multi) {
                    var single = element.split("=");
                    opinions.put(
                            URLDecoder.decode(single[0], StandardCharsets.UTF_8),
                            URLDecoder.decode(single[1], StandardCharsets.UTF_8)
                    );
                }
                return opinions;
            }
        } else {
            return new HashMap<>();
        }
    }
}
