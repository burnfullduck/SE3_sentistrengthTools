# 第二次根据checkstyle修改代码报告



## 一、报告概述

本次报告分析针对第二次checkstyle分析报告`分析报告\2\`至第三次checkstyle分析报告`分析报告\3\`的变化。

主要做了如下修改：

+ 将`public`成员变量封装为`private`并提供`getter`、`setter`方法
+ 处理了全部魔法数`Magic Number`，定义为常量
+ 将所有方法参数中应该为`final `的参数修改为`final`
+ 对不符合命名规范的方法、字段进行重构
+ 禁用工具类的构造方法
+ 对`weka`包和`Utility`包中的所有类进行checkstyle风格修改
  + 大括号`{}`使用规范
  + JavaDoc格式规范
  + 禁用tab，改为四个空格



## 二 、结果统计

|                                 | open | closed | new  |
| ------------------------------- | ---- | ------ | ---- |
| 总计                            | 1749 | 1738   | 0    |
| BoosterWordsList.java           | 17   | 10     | 0    |
| ClassificationOptions.java      | 58   | 47     | 0    |
| ClassificationResources.java    | 10   | 21     | 0    |
| ClassificationStatistics        | 28   | 19     | 0    |
| Corpus                          | 371  | 157    | 0    |
| CorrectSpellingsList            | 6    | 0      | 0    |
| EmoticonsList                   | 7    | 1      | 0    |
| EvaluativeTerms                 | 20   | 1      | 0    |
| IdiomList                       | 16   | 9      | 0    |
| IronyList                       | 4    | 0      | 0    |
| Lemmatiser                      | 9    | 0      | 0    |
| NegatingWordList                | 6    | 0      | 0    |
| Paragraph                       | 66   | 10     | 0    |
| QuestionWords                   | 7    | 0      | 0    |
| Sentence                        | 130  | 10     | 0    |
| SentimentWords                  | 56   | 4      | 0    |
| SentiStrength                   | 203  | 109    | 0    |
| Term                            | 69   | 9      | 0    |
| Test                            | 3    | 4      | 0    |
| TextParsingOptions              | 0    | 4      | 0    |
| UnusedTermsClassificationIndex  | 45   | 12     | 0    |
| FileOps                         | 3    | 18     | 0    |
| Sort                            | 11   | 62     | 0    |
| StringIndex                     | 22   | 61     | 0    |
| Trie                            | 12   | 37     | 0    |
| Arff                            | 264  | 453    | 0    |
| PredictClass                    | 55   | 74     | 0    |
| Utilities                       | 2    | 36     | 0    |
| WekaCrossValidateInfoGain       | 92   | 207    | 0    |
| WekaCrossValidateNoSelection    | 78   | 168    | 0    |
| WekaDirectTrainClassifyEvaluate | 53   | 108    | 0    |
| WekaMachineLearning             | 26   | 87     | 0    |



## 三、结果说明

### 3.1 open

| 严重程度 | 种类  | 规则            | 信息（举例）                   | 原因                                                         |
| -------- | ----- | --------------- | ------------------------------ | ------------------------------------------------------------ |
| Warning  | sizes | LineLength      | 本行字符数 137个，最多：80个。 | 还没有进入代码重构阶段，无法修改单行代码的长度。             |
| Error    | sizes | ParameterNumber | 参数共： 8个，最多：7个。      | 还没有进入代码重构阶段，不宜对函数直接修改，无法更改函数的参数个数。 |

除此之外的所有警告或缺陷均被解决，所以只有两种open的缺陷或警告类型。



### 3.2 close

| 严重程度 | 种类   | 规则                        | 信息（举例）                                                 | 解决办法                                                     |
| -------- | ------ | --------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Warning  | design | VisibilityModifier          | 变量 'bgSaveArffAsCondensed' 应定义为 private 的，并配置访问方法。 | 将`public`的成员变量封装为`private`，并提供`getter`,`setter`方法 |
| Warning  | coding | MagicNumber                 | '1.5F' 是一个魔术数字（直接常数）。                          | 将MagicNumber声明为常量。                                    |
| Warning  | misc   | FinalParameters             | 参数： iNegCorrect 应定义为 final 的。                       | 直接添加`final`修饰符，如果会产生错误，利用局部变量接收参数的值。 |
| Warning  | naming | MethodName                  | 名称 'printClassificationResultsSummary_NOT_DONE' 必须匹配表达式： '^[a-z][a-zA-Z0-9]*$' 。 | 按照规则重构方法名称。                                       |
| Error    | design | HideUtilityClassConstructor | 工具类应隐藏 public 构造器。                                 | 将构造器禁用。                                               |

其余的close在上一次的版本中均有提及，故此处没有再举例。

### 3.3 new

由于没有进行代码重构，没有引入新的警告或缺陷。

### 3.4 checkstyle没有警告但人工发现的缺陷

+ 部分`MagicNumber`没有被检测出。如在`Term`类中的数字1和2，应该表示的是单词类型和标点类型，但是checkstyle会自动忽略1与2的`MagicNumber`检测，所以没有检测出，已经修改为对应的常量表达。