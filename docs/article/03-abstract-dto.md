### Backend: 抽象简化 HTTP 请求和回应

使用 GSON 的 `JsonDeserializer<T>` 和 `JsonSerializer<T>`来自定义(反)序列化过程：

```java
public class AnswerRequestDTO {
    public String sessionKey = null;
    public boolean answerRight;

    public static AnswerRequestDTO deserialize(String data) {
        return GlobalGson.INSTANCE.fromJson(new JsonReader(new StringReader(data)), AnswerRequestDTO.class);
    }

    public static class Deserializer implements JsonDeserializer<AnswerRequestDTO> {
        @Override
        public AnswerRequestDTO deserialize(
                JsonElement json, Type typeOfT, JsonDeserializationContext context
        ) throws JsonParseException {
            AnswerRequestDTO obj = new AnswerRequestDTO();
            JsonObject jsonObject = json.getAsJsonObject();
            obj.sessionKey = jsonObject.get("sessionKey").getAsString();
            obj.answerRight = jsonObject.get("isRight").getAsBoolean();
            return obj;
        }
    }
}
```

在 `RequestDTO` 中定义一个子类 `Deserializer` 用来反序列化 json 字符串，通过

```java
RequestDTO.deserialize(String json)
```

就可以反序列化，得到一个包含特定属性的 `RequestDTO` 。

```java
public class AnswerResponseDTO implements IResponseDTO {
    public String result;

    public AnswerResponseDTO(String result) {
        this.result = result;
    }

    @Override
    public String serialize() {
        return GlobalGson.INSTANCE.toJson(this);
    }

    public static class Serializer implements JsonSerializer<AnswerResponseDTO> {
        @Override
        public JsonElement serialize(AnswerResponseDTO src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("result", src.result);
            return jsonObject;
        }
    }
}
```

在 `ResponseDTO` 中定义一个子类 `Serializer` 用来序列化 `ResponseDTO` 类的属性为 json 字符串，通过

```java
new ResponseDTO(/* field args */).serialize()
```

即可得到序列化后的字符串。

```java
public class GlobalGson {
    public final static Gson INSTANCE = (new GsonBuilder())
            //deserializers
            .registerTypeAdapter(AnswerRequestDTO.class, new AnswerRequestDTO.Deserializer())
            //serializers
            .registerTypeAdapter(AnswerResponseDTO.class, new AnswerResponseDTO.Serializer())
            .create();
}
```

下面是使用实例，很简单地处理请求内容和发送回应。

```java
@Route(path = "/v1/answer", method = RouteType.POST)
public class AnswerRoute implements IRouteHandler {
    @Override
    public ResponseContentWrapper handle(FullHttpRequest request) {
        var content = request.content().toString(StandardCharsets.UTF_8);
        var answerInfo = AnswerRequestDTO.deserialize(content);
        var session = SessionManager.INSTANCE.getSession(answerInfo.sessionKey);
        /* ... */
        return new ResponseContentWrapper(
            HttpResponseStatus.OK, 
            new AnswerResponseDTO("operation success.")
        );
    }
}
```

> 下一篇：[Backend: 封装数据库 DAO 模式](04-wrap-database-dao.md)
