package RainbowSix.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import RainbowSix.JDBC.Comment;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

@CrossOrigin
@RestController
@RequestMapping("/analysis")
public class AnalysisController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 模板函数
     * @return 列表
     */
    @GetMapping (value = "/getData",produces="application/json;charset=UTF-8")
    @ResponseBody

    public List<Comment> getData(){
        String sql="select * from superset";//SQL查询语句
        List<Comment> comments = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class));
        out.println(comments.toString());
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class));
    }

    /**
     * 按页获取所有数据
     * @return 返回某页的数据
     */
    @PostMapping(value = "/getAllDataByPage")
    public List<Comment> getAllDataByPage(@RequestParam("page") String page){
        int pagei = Integer.parseInt(page);
        int start = (pagei-1) * 50;
        int end = pagei * 50;
        String sql="select * from superset";
        List<Comment> comments = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class));
        return comments.subList(start,end);
    }

    /**
     * 按评论回复者名字获取所有数据
     * @return 返回该回复者的所有数据
     */
    @PostMapping(value = "/getAllDataByName")
    public List<Comment> getAllDataByName(@RequestParam("name") String name){
        char ws = '\"';
        String sql="select * from superset where Reviewer=" + ws + name + ws;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class));
    }

    /**
     * 按日期获取所有数据
     * @return 返回该回复者的所有数据
     */
    @PostMapping(value = "/getAllDataByDate")
    public List<Comment> getAllDataByDate(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){

        String sql="select * from superset where Time between " + "'"+ startDate +"'"+ " and " +"'"+ endDate+"'";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class));
    }

    /**
     * 计算出数据库中数据的senti值
     * @author ZhuJiahao
     * @return 成功初始化的数据条数
     * @throws IOException
     */
    @GetMapping(value = "/dataInitialization")
    @ResponseBody
    public String dataInitialization() throws IOException {
        String sql = "select * from superset where senti=9";//SQL查询senti为9的数据
        List<Comment> comments = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class));
        for(Comment comment : comments){
            String text = comment.getText().replace(" ","+"); //如果不replace，遇到带“”的字符串会报错
            if (text.equals(""))
                text="null";
            String[] cmd = {"java", "-jar", "senti/R6_senti-0.2_phase1_Done.jar", "sentidata", "senti/dict/", "text", text, "scale"};

//            out.println("*************************"+ Arrays.toString(cmd));
            // 调用外部jar包
            ProcessBuilder builder = new ProcessBuilder(cmd);
            builder.directory(new File("."));
            builder.redirectErrorStream(true);
            Process process = builder.start();
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            // 读取外部jar包输出并返回
            String line = reader.readLine();
//            out.println(line);
            int senti = Integer.parseInt(line.split(" ")[2]);

            String updatesql = "update superset set senti=? where TextNo=?";
            Object[] args = {senti, comment.getTextNo()};
            jdbcTemplate.update(updatesql,args);

        }

        return "succeed to update "+comments.size()+" data";
    }

    /**
     * 该项目每一天的评论数list，日期由早到晚
     * @author XuChen
     * @return 按评论日期聚类形成的评论数list
     */
    @GetMapping(value = "/projectReviewNum")
    @ResponseBody
    public List<Integer> projectReviewNum(){
        String sql = "select tot from(select CONCAT(YEAR(Time), '-', MONTH(Time)) as yearMonth, COUNT(*) as tot from superset group by yearMonth)a";
        return jdbcTemplate.queryForList(sql, Integer.TYPE);
    }

    /**
     * 按给定评论者得到其每一天的评论数list，日期由早到晚
     * @author XuCHen
     * @return
     */
    @PostMapping(value = "/personalReviewNum")
    @ResponseBody
    public List<Integer> personalReviewNum(@RequestParam("ReviewerName") String ReviewerName){
        String sql = "select COUNT(*) from superset where Reviewer="+ReviewerName+" group by senti";
        return jdbcTemplate.queryForList(sql, Integer.TYPE);
    }

    /**
     * 按senti值给出不同senti值数量的list，由低到高
     * @author XuChen
     * @return
     */
    @GetMapping(value = "/sentiReviewNum")
    @ResponseBody
    public List<Integer> sentiReviewNum(){
        String sql = "select COUNT(*) from superset group by senti";
        return jdbcTemplate.queryForList(sql, Integer.TYPE);
    }
    /**
     * @author XuChen
     * @return (yearMonth, num, posNum, negNum)的list
     */
    @GetMapping(value = "/dataEachMonth")
    @ResponseBody
    public List<Map<String, Object>> getDataEachMonth(){
        String sql = "select CONCAT(YEAR(Time), '-', MONTH(Time)) as yearMonth, COUNT(*) as num, sum(IF(senti>0,1,0)) as posNum, sum(IF(senti<0,1,0)) as negNum from superset group by yearMonth";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        return data;
    }

    /**
     * @author XuChen
     * @return (yearMonth, num, posNum, negNum)的list
     */
    @PostMapping(value = "/dataEachMonthByName")
    @ResponseBody
    public List<Map<String, Object>> getDataEachMonthByName(String name){
        String sql = "select CONCAT(YEAR(Time), '-', MONTH(Time)) as yearMonth, COUNT(*) as num, sum(IF(senti>0,1,0)) as posNum, sum(IF(senti<0,1,0)) as negNum from superset where Reviewer = '" + name +"' group by yearMonth";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        return data;
    }
}