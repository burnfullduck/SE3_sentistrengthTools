### 获取数据库中数据senti值的说明

1. 首先通过JDBCTemplate获取数据库中senti值未计算的数据（即senti值设为9的数据），将其保存为列表。Comment为自定义的数据库中存放的评论类。

```java
String sql = "select * from superset where senti=9";//SQL查询senti为9的数据
List<Comment> comments = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class));
```

2. 遍历上述列表，将Comment中的Text的空格替换为加号

   ```java
   String text = comment.getText().replace(" ","+"); //如果不replace，遇到带“”的字符串会报错
   ```

3. 循环调用sentistrength，计算出上述列表每个Comment的Text的senti值，之后通过JDBCTemplate，使用sql语句将该senti值更新在数据库中。

   ```java
   String[] cmd = {"java", "-jar", "senti/R6_senti-0.2_phase1_Done.jar", "sentidata", "senti/dict/", "text", text, "scale"};
               // 调用外部jar包
   ProcessBuilder builder = new ProcessBuilder(cmd);
   builder.directory(new File("."));
   builder.redirectErrorStream(true);
   Process process = builder.start();
    InputStream is = process.getInputStream();
   BufferedReader reader = new BufferedReader(new InputStreamReader(is));
   
   // 读取外部jar包输出并返回
   String line = reader.readLine();
   int senti = Integer.parseInt(line.split(" ")[2]);
   
   String updatesql = "update superset set senti=? where TextNo=?";
   Object[] args = {senti, comment.getTextNo()};
   jdbcTemplate.update(updatesql,args);
   ```

   