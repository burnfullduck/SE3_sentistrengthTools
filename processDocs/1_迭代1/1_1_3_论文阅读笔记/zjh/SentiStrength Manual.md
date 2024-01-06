## ***\*SentiStrength Manual\****

* 核心功能是使用基于字典的算法来分析文本的情感。具体来说，它首先根据情感词典给词分配先验情感分数，然后用若干启发式规则调整分配结果。

### 关键要素

1. UC-1为**词**赋情感分:算法的核心是情感词强度列表(SentiStrength中的EmotionLookupTable)。如果该单词在EmotionLookupTable中不存在，则默认为中性。值得注意的是，单词“miss”被分配了正负强度2。
2. UC-2为短语分配情感分数:IdiomLookupTable用于为通常包含多个单词的短语分配分数。
3. UC-3拼写纠正:一种算法通过包含重复字母来识别拼写错误的单词的标准拼写。
   * 算法(a)自动删除两次以上的重复字母(例如hello-> hello)；
   * (b)对于在英语中很少出现两次的字母，删除出现两次的重复字母(例如，niice -> nice)，
   * 以及(c)删除出现两次的字母，如果它们不是标准单词，但如果被删除将形成标准单词(例如，nnice -> nice但不是hoop -> hop nor baaz -> baz)。
   * 英语单词列表将用于检查单词的拼写是否正确。
4. UC-4增强词规则:• 每个单词增加1或2的情感强度(例如，非常，极端)或减少1(例如，一些)。
5. UC-5否定词规则:一个否定词表(negating word list)包含反转后续情感词的词(包括任何前面的助词)。
   * 一些否定术语不否定的可能性没有被纳入。
6. UC-6重复字母规则:只要有至少两个额外的字母，超过正确拼写所需的重复字母被用来给情感单词增加1的强度。如gooood。
7. UC-7表情符号规则:具有关联强度(正或负2)的表情符号列表(EmotionLookupTable)补充了情感单词强度列表
8. UC-8感叹号规则:任何带有感叹号的句子都被分配了**最低**为2的正强度。
9. UC-9重复标点规则:包括至少一个感叹号的重复标点给紧接在前面的情感词(或句子)增加1的强度。
10. UC-10忽略了问题中的**负面**情绪：
    * “你生气了吗？”将被归类为不包含情绪
    * “怎么了？”通常被归类为包含轻度积极情绪(强度2)

### 以下是完成不同的分类任务

11. UC-11对单文本分类
12. UC-12对文件中的所有文本**行**进行情感分类[包括准确性评估]
13. UC-13对文件或文件夹中某一栏的文本进行分类
    * 对于每一行，指定列中的文本将被提取和分类，并将结果添加到文件末尾的额外列中(这三个参数都是必需的)。：
      * annotateCol [col # 1..] (classify text in col, result at line end)
      * inputFolder [foldername] (all files in folder will be *annotated*)
      * fileSubstring [text] (string must be present in files to annotate)
14. UC-14在一个端口监听要分类的文本
15. UC-15从命令行交互运行
16. UC-16处理标准输入并发送到标准输出
    * SentiStrength将对从stdin发送给它的所有文本进行分类，然后关闭。

### 设置数据的位置

17. UC-17语言数据文件夹的位置
18. UC-18情感术语权重的位置
19. UC-19输出文件夹的位置
20. UC-20输出的文件扩展名

### 设置不同类型的输出

21. UC-21分别对正面(1到5)和负面(-1到5)情绪强度进行分类  默认设置
22. UC-22使用三元分类法(阳性-阴性-中性)  trinary
    * 3 -1 1。这就是:(+ve分类) (-ve分类)(三元分类)
23. UC-23使用二元分类法(正-负)  binary
    * 3 -1 1。这就是:(+ve分类) (-ve分类)(二元分类)
24. UC-24使用单一正负标度分类  scale
    * 标度(改为报告单-4到+4分类)

### 解释分类

25. UC-25解释分类
    * explain将此参数添加到大多数选项中会导致对分类给出一个大致的解释。
    * java -jar SentiStrength.jar text i+don't+hate+you. explain
26. UC-26设置分类算法参数 *
    * 这些选项中的大部分都可以映射到SentiStrength的**核心功能**。他们可以**改变情感分析算法的工作方式**。
    * 选项较多，详见文档。

### 提高SentiStrength的准确性

可手动改进

例如“恶心”一词在你的文本中通常比SentiStrength给出的情感强度更强或更弱，那么你可以用SentiStrength编辑文本文件来改变这一点。请使用纯文本编辑器编辑SentiStrength的输入文件，因为如果使用文字处理器编辑，SentiStrength可能无法读取该文件。

27. UC-27优化现有情感术语的情感强度
    * SentiStrength可以为EmotionLookupTable.txt建议修订的情感强度，以便为给定的一组文本给出更准确的分类。
    * 这个选项需要一个纯文本文件中的大量文本(> 500)，每个文本都有人类情感分类。SentiStrength将尝试调整EmotionLookupTable.txt术语权重，以便在对这些文本进行分类时更加准确。在对相似文本进行分类时，它也应该更加准确。
    * 如果输入文件很大(分别为数十万或数百万)，这将非常慢(数小时或数天)。
    * 主要的可选参数是minImprovement(默认值为2)。
    * 设置此项以指定更改情感术语权重的附加正确分类的最小总数。
28. 建议新的情感术语(来自错误分类文本中的术语)
    * SentiStrength可以建议将一组新的术语添加到EmotionLookupTable.txt中，以便对给定的一组文本进行更准确的分类。
29. 机器学习评估
    * *train* (evaluate SentiStrength by training term strengths on results in file). An input file of 500+ human classified texts is also needed - e.g.,  此为基本命令
    * 其他命令详见文档