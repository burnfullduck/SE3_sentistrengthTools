## 0 综述

面向开发协作网络的软件工程文本 情绪分析与应用

EASIEST - S**E**ntiment **A**nalysis and Related Application on **S**oftware Eng**I**neering T**E**xts from Collaborative **S**ocial Ne**T**works

研究背景：软工文本（所有在讨论软件的文本）包含了人的情绪。

情绪：由极性、强度两个维度组成的函数。分析情绪的方法：

**词典式方法**

- 根据 **情绪词典** 为词语分配先验性的情绪值，再以若干 **启发式规则** 调整分配结果。
- 典型工具：SentiStrength [M. Thelwall JASIST 2012] 、 SentiStrength-SE [M . R. Islam JSS 2018]
- 优化方向：生成更高质量的词典、总结更有效的启发式规则

**学习式方法**

- 基于 **机器学习** 的方法：基于支持向量机的 Senti4SD [F. Calefato ESE 2018]、基于梯度提升树的 SentiCR [T. Ahmed ASE 2017]
- 基于 **深度学习** 的方法：对比了多种预训练的 Transformer 模型 [T. Zhang ICSME 2020]、Transformer + TextCNN [K. Sun Internetware 2022]

SentiStrength是一个根据 **MySpace** **网站评论** 开发而来的 **词典式** 情绪分析工具；应用最广泛的情绪分析工具，**易于上手，结构简单，可扩展性强**。

## 1 论文1

#### 1、Abstract

情感分析关注“从文本中自动提取情感相关信息”。本文评估了SentiStrength算法的一个改进版本（也许可以借鉴）。另外，SentiStrength在新闻相关讨论中的积极情绪方面尤其弱；无论有无监督，SentiStrength都很健壮，可适用于多种不同场景。

#### 2、Intro

情感分析集中于对电影、消费品的评论，但也有很多媒体评论、论坛交流等。

部分分类器可能受间接的情绪指标得出错误的结论，如中东地区可能被标记为Negative。利用词典方法可缓解。

#### 3、Sentiment Analysis

情感分析的一种常见方法是选择一种机器学习算法和一种从文本中提取特征的方法，然后用人工编码的语料库训练分类器。所使用的特征通常是单词，但也可以是词干词或词性标记词，也可能被组合成二元分词。

另一种极性检测方法是通过估计它们与一组已知且无歧义情感的种子词共现的频率来识别文本中单词可能的平均极性，通常使用网络搜索引擎来估计相对共现频率。

不直接含有语义的词，如late，feel等是间接情感词，区别于直接情感词。间接情感词的使用对于某些类型的社会科学情感分析研究和一些商业应用来说是一个缺点，因为它使方法依赖于领域，有时也依赖于时间。

#### 4、算法

朴素贝叶斯方法（机器学习之一）方法不如词汇方法，虽然这并不代表机器学习不如词汇。

#### 5、SentiStrength2

（a，b）表积极程度为a，消极程度为b。可认为其携带的情绪是a-b。

有有监督、无监督两种。

本文中有SentiStrength的情感打分规则详细说明。

#### 6、Research Question

本研究的目标是评估各种不同的在线上下文中的SentiStrength2，以确定它作为社交网络的通用情感强度检测算法是否可行.

- 无监督版本是否与所有类型的社交网络文本具有显著的正相关，无论是积极的还是消极的情绪?
- 监督版本是否与所有类型的社交网络文本都有显著的正相关，无论是积极的还是消极的情绪?
- 在社交网络文本上的性能是否优于标准的机器学习算法?

#### 7、方法和数据、结果

采用多名Coder用自己的判断预处理数据。

庞大的合并数据集中，性能较低，可能因为Weka的限制（48G的Ram给了JAVA虚拟机）。

尽管有监督的情感强度往往比无监督的情感强度更准确，但它们在关键相关性检验中大致相等。这表明，监督(创建和使用训练数据来优化词条权重)对于与表中类似的应用领域来说是不必要的。

机器学习方法，特别是逻辑回归，略好于SentiStrength。

#### 8、Limitations and Discussion

问题：

- 6个比同属性的数据集很多，但并不全面。
- 并非所有的数据集都由三个处理人员共同处理的。
- 机器学习算法的性能可能被夸大了。
- SentiStrength对直接情感词的依赖程度更高

结论：

- 有无监督版的SentiStrength都很强大。
- 2012年的机器学习可能优于SentiStrength（2023年了，感觉ML已经远超SS了）。
- SentiStrength似乎适用于社交网络中的情感强度检测，即使是在无监督的版本中，也推荐用于仅利用直接情感词的应用。它的主要弱点是检测讽刺，因此这是未来研究的一个合乎逻辑的方向。
- SS的成功说明了词典方法也有很强的可行性。

## 2 论文2

#### 1、Abstract & Intro

SS使用新的方法来利用网络空间事实上的语法和拼写风格，从非正式英语文本中提取情感强度，预测消极或积极情绪。

在线情感检测的一个复杂因素是，在许多电子通信媒体中，基于文本的英语通信似乎经常忽略语法和拼写规则。另外同样的词可能有正面负面等不同的情感（结合上下文）。最后，还有一些新的表情符号。

感觉这篇文章和上一篇背景差不多，而且还都用了MySpace等数据集。

#### 2、Background

观点挖掘和意见挖掘，用ML识别与消极、积极相关的一般特征。

- 特征选择，去除最无用的n-gram数据。

- 分类算法：大量使用SVM（分类向量机？不如不翻译）。

- 基于语言学方法、组合语义等。

检测多种可能存在的情绪。确定极性。

监测情感强度。

#### 3、数据集&人类的判断

和论文一差不多，都是拿数据集来给人类判断，好作为相对的“标准答案”和后面的

#### 4、算法

算法的核心是情感词强度表。这是298个正面词语和465个负面词语的集合，被分类为正面或负面情绪强度，值从2到5。默认分类是基于开发阶段的人工判断，在训练阶段会自动修改(见下文)。

“miss”这个词的正负强度分别为2。这是唯一一个既被归类为肯定又被归类为否定的词。它通常用在短语“我想你”中，暗示悲伤和爱。

#### 5、结论

SentiStrength相对成功的主要原因似乎是解码非标准拼写的程序和增强单词强度的方法，这是其性能的主要原因。在没有这些因素的情况下，仅基于情感相关词汇词典的SentiStrength变体及其57.5%的估计强度仅比最成功的机器学习方法在1-3克的扩展集上好1.3%。

## 3 论文3

SentiStrength最初是以jar包的形式发布的。现在也能下载到。

#### 1、核心功能

使用一套字典和若干启发式规则对文本进行情感分析。

EmotionLookupTable。对单词和通配符分配请安分数。

IdiomLookupTable。对短语分配分数。

BoosterWordList。包含增强或降低后续词的情绪的词。

NegatingWordList。包含反转后续情感词的词。

EmotionLookupTable。表情符号列表。

#### 2、完成不同的分类任务

SentiStrength可以对单个文本或多个文本进行分类，并且可以以多种不同的方式调用。

* 对单个文本进行分类。

  * text[text to process]。

* 对文件中的所有文本进行情感分类【包括准确性评估】。

  * input[filename]。

* 对文件或者文件夹中某一栏的文本进行分类。

  * annotateCol [col# 1..] inputFolder[foldername]  fileSubstring [text]

* 在一个端口监听要分类的文本。

  * listen[port number to listen at - call OR

* 从命令行交互运行。

  * cmd (can also set options and sentidata folder)

* 处理标准输入并发送到标准输出

  * stdin (can also set options and sentidata folder)

* 语言数据文件夹的位置

  * sentidata [folder for SentiStrength data (end in slash, no spaces)]

* 情感术语权重的位置

  * EmotionLookupTable [filename (default: EmotionLookupTable.txt）

* 输出文件夹的位置

  * outputFolder [foldername where to put the output (default: folder of input)]

* 输出的UC-20文件扩展名

  * resultsextension [file-extension for output (default _out.txt)]

* 分别对正面(1到5)和负面(-1到5)情绪强度进行分类

* 使用三元分类法(阳性-阴性-中性)

  * trinary (report positive-negative-neutral classification instead)

* 使用二元分类法(正-负)

  * binary (report positive-negative classification instead)

* 使用单一正负标度分类

  * scale (report single -4 to +4 classification instead)

#### 3、解释分类

设置分类算法参数

* alwaysSplitWordsAtApostrophes 拆分

* noBoosters (ignore sentiment booster words (e.g., very)) 无加强

* noNegatingPositiveFlipsEmotion (don't use negating words to flip +ve words) 不用否定词反转

* noNegatingNegativeNeutralisesEmotion (don't use negating words to neuter -ve words) 不用否定词中性化

* negatedWordStrengthMultiplier (strength multiplier when negated (default=0.5)) 求反时的强度乘数

* maxWordsBeforeSentimentToNegate (max words between negator & sentiment word (default 0)) 否定词和情感词之间的最大字数

* noIdioms (ignore idiom list) 忽略习语表

* questionsReduceNeg (-ve sentiment reduced in questions) 问题减少负面情绪

* noEmoticons (ignore emoticon list) 无表情图标

  。。。。

#### 4、、提高SentiStrength的准确性(2)

* 优化现有情感术语的情感强度

  * optimise [Filename for optimal term strengths (e.g. EmotionLookupTable2.txt)]

* 建议新的情感术语(来自错误分类文本中的术语)

  * termWeights

#### 5、机器学习评估

* all  测试上述分类算法参数中列出的所有选项变体

* tot 通过正确分类的数量而不是分类差异的总和进行优化

* iterations [number of 10-fold iterations (default 1)] 迭代次数

* minImprovement [min extra correct class. to change sentiment weights (default 2)] 设置在训练阶段调整术语权重所需的额外正确分类的最小数量。

* multi [# duplicate term strength optimisations to change sentiment weights (default 1)] 术语权重不是被优化一次，而是从起始值被优化多次，然后取这些权重的平均值，并被优化和用作最终优化的术语强度。

  