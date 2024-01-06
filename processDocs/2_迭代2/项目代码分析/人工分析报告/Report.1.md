## Report.1

### 一、报告背景

* 使用的checkstyle规则是在sun_check基础上自定义的规则

* 本次报告分析针对第二次checkstyle分析报告`分析报告\1\`至第三次checkstyle分析报告`分析报告\2\`的变化。
* 第一次使用checkstyle的代码修改在**迭代一**进行，故主要是**针对javaDoc方面**的修改。

### 二、结果统计

|                                      | open | closed | new  |
| ------------------------------------ | ---- | ------ | ---- |
| 整个项目（共35个文件）               | 3487 | 423    | 0    |
| BoosterWordsList.java                | 27   | 0      | 0    |
| ClassificationOptions.java           | 105  | 11     | 0    |
| ClassificationResources.java         | 31   | 8      | 0    |
| ClassificationStatistics.java        | 47   | 0      | 0    |
| Corpus.java                          | 528  | 274    | 0    |
| CorrectSpellingsList.java            | 6    | 0      | 0    |
| EmotionsList.java                    | 8    | 11     | 0    |
| EvaluativeTerms.java                 | 21   | 17     | 0    |
| IdiomList.java                       | 25   | 25     | 0    |
| IronyList.java                       | 4    | 11     | 0    |
| Lemmatiser.java                      | 9    | 1      | 0    |
| NegatingWordList.java                | 6    | 8      | 0    |
| Paragraph.java                       | 76   | 23     | 0    |
| QuestionWords.java                   | 7    | 1      | 0    |
| Sentence.java                        | 140  | 3      | 0    |
| SentiStrength.java                   | 312  | 1      | 0    |
| SentimentWords.java                  | 60   | 1      | 0    |
| Term.java                            | 78   | 13     | 0    |
| Test.java                            | 7    | 9      | 0    |
| TextParsingOptions.java              | 4    | 0      | 0    |
| UnusedTermsClassificationIndex.java  | 57   | 3      | 0    |
| FileOps.java                         | 21   | 1      | 0    |
| Sort.java                            | 73   | 0      | 0    |
| StringIndex.java                     | 83   | 0      | 0    |
| Trie.java                            | 49   | 0      | 0    |
| Arff.java                            | 717  | 2      | 0    |
| PredictClass.java                    | 129  | 0      | 0    |
| Utilities.java                       | 38   | 0      | 0    |
| WekaCrossValidateInfoGain.java       | 299  | 0      | 0    |
| WekaCrossValidateNoSelection.java    | 246  | 0      | 0    |
| WekaDirectTrainClassifyEvaluate.java | 161  | 0      | 0    |
| WekaMachineLearning.java             | 113  | 0      | 0    |

### 三、具体分析

* 修改前提交的checkstyle结果对于整个项目(35个java文件)共有**3910**个警告，其中**3824**个为**warning**，**86**个为**error**。主要警告如下：（具体详见 `\项目代码分析\分析报告\1\checkstyle.html`）

  1. 对区域的检查(block)，有LeftCurly, NeedBraces和RightCurly。
  2. 编码检查(coding)，有HiddenField, InnerAssignment, MagicNumber和MissingSwitchDefault。
  3. 类设计检查(design)，有HideUtilityClassConstructor和VisibilityModifier。
  4. 插入文件检查(import)，只有AvoidStarImport。
  5. javaDoc检查(javadoc)，有JavadocMethod, JavadocPackage, JavadocStyle, 	JavadocVariable和MissingJavadocMethod。
  6. 其他(Miscellaneous)，有ArrayTypeStyle和FinalParameters。
  7. 命名规则检查(naming)，有ConstantName, LocalVariableName和MethodName。
  8. 正则表达式检查(regexp)，只有RegexpSingleline。
  9. 长度限制检查(sizes)，有FileLength, LineLength和ParameterNumber。
  10. 空格检查(whitespace)，EmptyForIteratorPad, FileTabCharacter, WhitespaceAfter和WhitespaceAround。

  * 对于上述不同的警告我们设置了不同的优先级属性，详见sentiStrength项目中的.setting\s

* 修改后提交的checkstyle结果对于整个项目(35个java文件)共有**3487**个警告，其中**3448**个为**warning**，**39**个为**error**。

* 本次修改主要修改了**sentiStrength包**内的代码，主要排除了以下警告(即**closed**)：

  1. 对区域的检查(block)中的所有NeedBrace警告，主要是对于if else强制加上了大括号。
  2. 排除了MissingSwitchDefault警告，加上了switch代码的default。
  3. 排除了全部的AvoidStarImport警告，将所有的星号插入转换为具体的文件插入。
  4. 本次修改主要面向javaDoc，故将javaDoc方面的所有警告都进行了相应处理。比如按照javadocStyle修改了javadoc编写风格，加上了私有方法的javadoc，加上了包相关的javadoc，补全了所有类的成员变量的javadoc。
  5. 排除了ArrayTypeStyle警告，全部改为java的ArrayType。并且修改了一部分的FinalParameters警告，剩下的final变量我们暂时认为没有问题。
  6. 排除了RegexpSingleline警告，主要是删去了多余的空格。

* 尚未排除的警告(即**open**)：

  1. wka和utilities包内代码暂未修改。
  2. whitespace 空格相关的警告由于之后需要重构，故暂未修改。
  3. sizes 长度限制相关的警告由于之后需要重构，故暂未修改。
  4. naming 命名规则检查相关的警告由于之后需要重构，故暂未修改。
  5. FinalParameters 方面的警告有一些我们小组认为属于误报，修改会导致程序运行失败，故暂未修改。
  6. 类设计方面由于之后需要重构，故暂未修改。
  7. coding 编码检查方面的警告也等待之后修改。

* 由于本次修改主要面向迭代一，故主要修改了javadoc方面的警告，并没有对代码结构发生改变，故没有新增警告(即**new**)