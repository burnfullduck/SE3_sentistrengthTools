# SentiStrength Manual

**手册**

## Introduce

核心功能是使用基于字典的算法来分析文本的情感。

为每个文本输入给出一个情感的得分（a，b），a 1~5 正面情绪 ；b -1~-5：负面情绪



## Core Function

使用一套字典和若干启发式规则对文本进行情感分析



### **UC-1 Assigning** **Sentiment** **Scores for Words**:

给单词分配情感得分。根据**sentiment** **word** **strength** **list** （EmotionLookupTable）情感单词强度表一一对应分数，如果没有，默认为中性



### **UC**-**2** **Assigning Sentiment Scores for Phrases**:

给短语分配情感得分。根据IdiomLookupTable 分配。



### UC-3 **Spelling Correction**:

拼写纠正。一种通过重复字母来识别拼写错误的单词的标准拼写算法。



### **UC-4 Booster Word Rule**:

增强词语规则。一个增强词列表(booster word list)包含增强或降低后续词的情绪的词，无论是积极的还是消极的。



### **UC-5 Negating Word Rule**: 

否定词规则。一个否定词表(negating word list)包含反转后续情感词的词(包括任何前面的助词)。



### **UC-6 Repeated** **Letter** **Rule**:

重复字母规则。只要有至少两个额外的字母，超过正确拼写的重复字母会给情感单词增加1分的强度。



### **UC-7 Emoji** **Rule**:

表情符号规则：具有关联强度(正或负2)的表情符号列表(EmotionLookupTable)补充了情感单词强度列表。



### **UC-8 Exclamation** **Mark** **Rule**: 

感叹号规则:任何带有感叹号的句子都被分配了最低为2的正强度。



### **UC-9 Repeated** **Punctuation** **Rule**:

重复标点规则。包括至少一个感叹号的重复标点给紧接在前面的情感词(或句子)增加1分的强度。



### **UC-10 Negative** **Sentiment** **Ignored** **in** **Questions**

忽略问句中的负面情绪。



## Other Funciton ------ Complete Different Classification Tasks（6）



### UC-11 Classify a single text

对单个文本进行分类

提交的文本将被分类，结果以+ve-space-ve的形式返回。如果分类方法是三进制、二进制或比例，那么结果将具有+ve-空间-ve-空间-整体的形式。





### UC-12 Classify all lines of text in a file for sentiment [includes accuracy evaluations]

对文件中的所有文本行进行情感分类[包括准确性评估]



### UC-13 Classify texts in a column within a file or folder

对文件或文件夹中某一栏的文本进行分类



### UC-14 Listen at a port for texts to classify

在一个端口监听要分类的文本



### UC-15 Run interactively from the command line

命令行交互运行



### UC-16 Process stdin and send to stdout

处理标准输入并发送到标准输出





## Other Function ------ Set Location of Data（4）



### UC-17 Location of linguistic data folder

语言数据文件夹的位置。该选项用于设置工具在哪个文件夹中搜索分析所需的词典(如EmotionLookupTable、IdiomLookupTable等)。



### UC-18 Location of sentiment term weights

情感术语权重的位置。核心情感强度列表的位置，默认值为EmotionLookupTable.txt或SentimentLookupTable.txt，该文件必须位于sentidata指定的目录中。



### UC-19 Location of output folder

输出文件夹的位置。该选项用于设置放置输出的文件夹的名称。



### UC-20 File name extension for output

输出文件的扩展名。此选项用于设置将使用什么标识符来标记输出文件。其默认值为“_out.txt”。



## Other Function ------Set Different Type of Output (4)



### UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment strength separately

分别对正面(1到5)和负面(-1到5)情绪强度进行分类.



### UC-22 Use trinary classification (positive-negative-neutral)

使用三元分类法(积极，消极，中立）



### UC-23 Use binary classification (positive-negative)

使用二元分类法(积极，消极）



### UC-24 Use a single positive-negative scale classification

使用单一正负标度分类



### UC-25 Explain the classification

解释分类



### UC-26 Set Classification Algorithm Parameters

设置分类算法参数





## Other Function ------ Improving the accuracy of SentiStrength(2)



### UC-27 Optimise sentiment strengths of existing sentiment terms

优化现有情感术语的情感强度



### UC-28 Suggest new sentiment terms (from terms in misclassified texts)

提出新的情感术语(来自错误分类文本中的术语)



### UC-29 Machine learning evaluations

机器学习评估