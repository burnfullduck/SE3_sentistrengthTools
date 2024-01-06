# 第三次根据checkstyle修改代码报告



## 一、报告概述

本次报告分析针对第三次checkstyle分析报告`分析报告\3\`至第四次checkstyle分析报告`分析报告\4\`的变化。

主要做了如下修改：

+ 对代码结构进行了重构优化。故基本上都是new

## 二 、结果统计

|                                   | open  | closed | new   |
| --------------------------------- | ----- | ------ | ----- |
| 总计                              | 1749  | 3      | 74    |
| BinaryMode.java (新建类)          | 0     | 0      | 2     |
| Mode.java (新建类)                | 0     | 0      | 32    |
| NegPosMode.java (新建类)          | 0     | 0      | 2     |
| ScaleMode.java (新建类)           | 0     | 0      | 11    |
| TrinaryMode.java (新建类)         | 0     | 0      | 11    |
| BoosterWordsList.java             | 17    | 0      | 1     |
| ClassificationOptions.java        | 58    | 0      | 0     |
| ClassificationResources.java      | 10    | 0      | 1     |
| ClassificationStatistics          | 28    | 0      | 0     |
| Corpus                            | 371   | 0      | 3     |
| CorrectSpellingsList              | 6     | 0      | 0     |
| EmoticonsList                     | 7     | 0      | 0     |
| EvaluativeTerms                   | 20    | 0      | 0     |
| IdiomList                         | 16    | 0      | 7     |
| IronyList                         | 4     | 0      | 0     |
| Lemmatiser                        | 9     | 0      | 0     |
| NegatingWordList                  | 6     | 0      | 0     |
| Paragraph                         | 66    | 0      | 0     |
| QuestionWords                     | 7     | 0      | 0     |
| Sentence                          | 130   | 0      | 0     |
| SentimentWords                    | 56    | 0      | 0     |
| SentiStrength                     | 203   | 0      | 4     |
| Term                              | 69    | 0      | 0     |
| ~~Test~~    （已删除）            | ~~3~~ | ~~4~~  | ~~0~~ |
| ~~TextParsingOptions~~ （已删除） | ~~0~~ | ~~4~~  | ~~0~~ |
| UnusedTermsClassificationIndex    | 45    | 0      | 0     |
| FileOps                           | 3     | 0      | 0     |
| Sort                              | 11    | 0      | 0     |
| StringIndex                       | 22    | 0      | 0     |
| Trie                              | 12    | 0      | 0     |
| Arff                              | 264   | 0      | 0     |
| PredictClass                      | 55    | 0      | 0     |
| Utilities                         | 2     | 0      | 0     |
| WekaCrossValidateInfoGain         | 92    | 0      | 0     |
| WekaCrossValidateNoSelection      | 78    | 0      | 0     |
| WekaDirectTrainClassifyEvaluate   | 53    | 0      | 0     |
| WekaMachineLearning               | 26    | 0      | 0     |



## 三、结果说明

### 3.1 open（与上次报告一致）

| 严重程度 | 种类  | 规则            | 信息（举例）                       | 原因                                   |
| -------- | ----- | --------------- | ---------------------------------- | -------------------------------------- |
| Warning  | sizes | LineLength      | 本行字符数 137个，最多：80个。     | 为了可读性，暂时不修改单行代码的长度。 |
| Error    | sizes | ParameterNumber | 参数共： 8个，最多：7个。          | 无法更改函数的参数个数。               |
| Warning  | sizes | FileLength      | 文件 2,097 行 （最多：2,000 行）。 | 为了文件可读性，不去刻意压缩文件行数   |

除此之外的所有警告或缺陷均被解决，所以只有两种open的缺陷或警告类型。



### 3.2 close

删除了Test类和TestParsingOptions类，close了3个警告

### 3.3 new

本次新增的警告集中于Mode等几个新建的类，主要警告类型有以下几种

| 严重程度 | 种类       | 规则               | 信息（举例）                                                 |
| -------- | ---------- | ------------------ | ------------------------------------------------------------ |
| Warning  | javadoc    | JavadocStyle       | Javadoc 首句应以句号结尾。                                   |
| Warning  | javadoc    | JavadocVariable    | 缺少 Javadoc 。                                              |
| Warning  | design     | VisibilityModifier | 变量 'trinaryValue' 应定义为 private 的，并配置访问方法。    |
| Warning  | design     | DesignForExtension | 類 'Mode' 看起來像是為擴展設計的（可以是子類），但方法 'setC' 沒有javadoc，解釋瞭如何安全地執行。如果類不是為擴展而設計的，請考慮創建類 'Mode' final或使方法 'setC' static/final/abstract/empty，或為方法添加允許的 |
| Warning  | misc       | FinalParameters    | 参数： c 应定义为 final 的。                                 |
| Warning  | whitespace | WhitespaceAround   | '{' 前应有空格。                                             |
| Error    | coding     | HiddenField        | 'c' 隐藏属性。                                               |

### 四、sonarlint检测结果

自从本次检测，我们小组使用了sonarlint对代码进行二次检测，其报出了一些checkstyle没有检测到的警告，且对代码进行了更加详尽的分析，具体见 [sonarlint结果报告3](.\分析报告\4\sonarlint结果报告.pdf )。

相较于checkstyle，sonarlint报告的警告更加全面，更加严格，规则与checkstyle有很大的不同。且其配套的分析过程已非常成熟，在企业中得到广泛的应用。





