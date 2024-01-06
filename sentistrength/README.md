# SentiStrength<br />

* 它为 Mike Thelwall 等人根据 MySpace 网站数据开发的社交文本情绪分析工具；<br>
* Mike Thelwall 等人最先于2010年的发表的论文 [*Sentiment Strength Detection in Short Informal Text*](https://doi.org/10.1002/asi.21416)
  中提出了该工具；后来，他们对更多种类的社交文本进行了探索，并形成了论文 [*Sentiment Strength Detection for the Social Web*](https://doi.org/10.1002/asi.21662) 于2012年发表。
  若您想细致的了解该工具，推荐优先阅读 *Sentiment Strength Detection in Short Informal Text* ，其中的描述更为详细。
* 该工具的官网地址为：http://sentistrength.wlv.ac.uk；<br>
* 官网提供了该工具的原版jar包，各种使用手册，以及可以试运行的demo等。除此之外，它还罗列了与该工具有关的若干论文，并提供了工具开发过程中标注的数据集。若有任何疑问，可优先查看其官网。<br>
* **该项目由反编译官网发布的jar包得来。**


运行参数：sentidata ./src/main/dict/ text i+like+you explain

jar包运行参数： java -jar target\R6_senti-0.2_phase1_Done.jar sentidata src\main\dict\ text i+like+you explain
注意在sentiStrength文件夹内运行此代码

MF需修改：Main-Class: rainbowsix.ss.SentiStrength

utilities中 将HelpOld，SentiStrengthOld，SentiStrengthTestAppletOld全部都注释了
这是为了更正版本为jdk17

添加了SpringBoot3，然后由于SP3自带Maven插件，所以不需要shade也可以。（必须去除Shade，不然会冲突）

Jenkins安装教程：https://www.cnblogs.com/lc-blogs/p/17005446.html

服务器管理者：ZXK
公网IP：124.221.102.208。
登录语法：ssh ubuntu@124.221.102.208
登录密码：zxk1019@!
JavaDoc在线网址：http://124.221.102.208/

加在JavaDoc的index.html的话：`<i>RainbowSix组 JavaDoc已上云！</i>`