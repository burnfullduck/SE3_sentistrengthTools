SentiStrength 为 Mike Thelwall 等人根据 MySpace 网站数据开发的社交文本情绪分析工具；
其官网地址为：http://sentistrength.wlv.ac.uk
官网提供了该工具的原版jar包，各种使用手册，以及可以试运行的demo等。除此之外，它还罗列了与该工具有关的若干论文，并提供了工具开发过程中标注的数据集。若有任何疑问，可优先查看其官网。
目前，其官网jar包的反编译结果以发布于：https://github.com/skx980810/SentiStrength

Mike Thelwall最先于2010年的发表的论文《Sentiment Strength Detection in Short Informal Text》中提出了该工具；
后来，他们对更多种类的社交文本进行了探索，并形成了论文《Sentiment Strength Detection for the Social Web》于2012年发表。推荐优先阅读《Sentiment Strength Detection in Short Informal Text》，其中的描述更为细节。

SentiStrength Manual：为结合了论文《Sentiment Strength Detection in Short Informal Text》，以及官网给出的使用手册而形成的用户手册。该文档中共包含了29个UC,其中UC-1～UC-10对应着SentiStrength算法的核心部分；UC-11～UC-29对应着其他功能。

阅读SentiStrength Manual和反编译代码后，需填写需求-代码矩阵.csv。该csv中的第一列为包中的各个类，第一行为29个UC，若认为某个类和UC之间存在映射关系，请在对应的单元格内打“×”。