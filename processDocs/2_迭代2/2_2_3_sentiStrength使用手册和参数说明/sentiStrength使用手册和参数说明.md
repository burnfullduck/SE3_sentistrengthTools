WriteBy 徐晨



## 情绪分类任务

### 分类单个文本

java -jar SentiStrength.jar sentidata [数据所在目录] text [单个文本（字符串）]
结果是+ve +ve，或者若方法是三元、二元、单指标则结果是+ve +ve overall

### 分类一个文件内所有行（包括精确评估）

java -jar SentiStrength.jar sentidata [数据所在目录] input [文件路径]

会创建一个新文件，在每一行后面加上了情绪分类。若任务是测试SentiStrength准确性，那么文件第一列为+ve，第二列为负值，第三列为文本。若使用二元、三元、单指标分类，那么第一列可以包含人类编写的值。列表须用制表符分隔。若人类编写的情绪分数包含在文件中，那么SentiStrength分类的值会与其进行比较

### 分类文件或文件夹中一列的文本

对每一行提取指定列中的文本进行分类，结果添加到文件末尾的额外列中（三个参数必选）

```
annotateCol [col # 1..] （分类col中的文件，结果跟在行尾）
inputFolder [folderName] （所有文件均会被注释）
fileSubstring [text]（字符串必须存在于文件中以注释卡）
Ok to overwrite files [overwrite]
```

如果指定的是文件夹而不是文件名，那么所有文件像上面一样处理。如果指定列fileSubstring值，那么只有匹配字符串的文件才会被分类。必须指定参数overwrite以显式允许修改输入文件

java -jar SentiStrength.jar sentidata [数据所在目录] anotateCol [列] inputFolder [] fileSubstring []

### 在端口监听要分类的文本

listen [端口号]

从命令行交互运行

cmd（同样可以设置选项和sentidata文件夹）

java -jar SentiStrength.jar cmd sentidata [数据所在目录]

这允许程序根据命令行输入对文本进行分类。运行此程序后，输入的每一行都将根据情绪进行分类。最后输入@end

### 处理stdin并发送到stdout

stdin（同样可以设置选项和sentidata文件夹）

java -jar SentiStrength.jar stdin sentidata [数据所在目录]

SentiStrength将从stdin发送给它的所有文本进行分类，然后关闭。这可能是将SentiStrength与非java程序有效集成的最有效方法。替代方案是在端口选项上侦听，或将要分类的文本转储到文件中，然后在文件上运行SentiStrength

如果数据以多个制表符分隔的列发送，并且其中一列包含要分类的文本，则可以设置参数textCol [第一列默认为0]
结果将被追加到输入数据的末尾，并发送给stdout

### 导入jar文件以在java程序中运行

导入jar，通过向public类SentiStrength中的main发送命令来初始化，然后调用computeSentimentScores()来处理每个文本

导入jar并创建类后的示例代码

```java
public class SentiStrengthApp {
    public static void main(String[] args) {
        //Method 1: one-off classification (inefficient for multiple classifications)
        //Create an array of command line parameters, including text or file to process
        String ssthInitialisationAndText[] = {"sentidata", "f:/SentStrength_Data/", "text", "I+hate+frogs+but+love+dogs.", "explain"};
        SentiStrength.main(ssthInitialisationAndText); 

        //Method 2: One initialisation and repeated classifications
        SentiStrength sentiStrength = new SentiStrength(); 
        //Create an array of command line parameters to send (not text or file to process)
        String ssthInitialisation[] = {"sentidata", "f:/SentStrength_Data/", "explain"};
        sentiStrength.initialise(ssthInitialisation); //Initialise
        //can now calculate sentiment scores quickly without having to initialise again
        System.out.println(sentiStrength.computeSentimentScores("I hate frogs.")); 
        System.out.println(sentiStrength.computeSentimentScores("I love dogs.")); 
    }
}
```

要实例化多个分类器，可以分别启动和初始化每个分类器

```java
SentiStrength classifier1 = new SentiStrength();
SentiStrength classifier2 = new SentiStrength();
//Also need to initialise both, as above
String ssthInitialisation1[] = {"sentidata", "f:/SentStrength_Data/", "explain"};
classifier1.initialise(ssthInitialisation1); //Initialise
String ssthInitialisation2[] = {"sentidata", "f:/SentStrength_Spanish_Data/"};
Classifier2.initialise(ssthInitialisation2); //Initialise
// after initialisation, can call both whenever needed:
String result_from_classifier1 = classifier1.computeSentimentScores(input);
String result_from_classifier2 = classifier2.computeSentimentScores(input);
```

## 提高SentiStrength的准确性

### 手动

如果在结果种看到一个分类模式，如术语”disgusting“通常在文本中具有比SentiStrength给出的更强或者更弱的情感强度，就可以用SentiStrength编辑文本文件来改变（用纯文本编辑器）

### 优化现有情感术语的情感强度

SentiStrength可以为EmotionLookupTable.txt建议修改后的情绪强度，以便对给定的文本集进行更准确的分类。这个选项需要一个大的(>500)纯文本文件中的文本集，每个文本都有一个人类情感分类。然后SentiStrength将尝试调整EmotionLookupTable.txt词条权重，以便在对这些文本进行分类时更加准确

```
optimise [优化术语强度的文件路径]
eg.
java -jar c:/SentiStrength.jar minImprovement 3 input C:/twitter4242.txt optimise C:/twitter4242OptimalSentimentLookupTable.txt
```

如果输入文件很大，则会非常慢。可选参数是minImprovement(默认值为2)。设置这个参数来指定改变情感词权重的额外正确分类的最小总数量。例如，如果将”love“的情感强度从3增加到4，使正确分类文本的数量从500提高到502，那么如果minImprovement是1或2，则此更改将保持，但如果minImprovement是>2，则拒绝。将这个值设置得更高，可以对字典进行更稳健的更改。更大的输入文件可以设置更高的值。

检查新字典的性能可以使用它而不是原来的SentimentLookupTable.txt，如：

```
java -jar c:/ sentimentlookuptable .jar输入c:/ twitter4242.txt EmotionLookupTable c:/ twitter4242OptimalSentimentLookupTable.txt
```

### 建议新的情感术语(来自错误分类文本中的术语)

SentiStrength可以建议一组新的术语添加到EmotionLookupTable.txt中，以便对给定的文本集给出更准确的分类。这个选项需要在一个纯文本文件中包含大量(>500)文本集，并为每个文本进行人类情感分类。然后SentiStrength将列出在EmotionLookupTable.txt中未找到的词，这些词可能表明情感。添加其中一些术语应该会使SentiStrength在对相似文本进行分类时更加准确。

termWeights列出了数据集中的所有术语，以及它们出现在错误分类的正面或负面文本中的比例。将其加载到一个电子表格中，并对PosClassAvDiff和NegClassAvDiff进行排序，以了解哪些词条应该被添加到情感词典中，因为这两个值中有一个很高。这个选项还会列出已经存在于情感词典中的单词。必须与包含正确分类的文本文件一起使用。例如

```
java -jar c:/SentiStrength.jar input C:/twitter4242.txt termWeights
```

## 选项

### 解释分类

explain

将此参数添加到大多数选项中，可以得到分类的大概解释

```
java -jar SentiStrength.jar text i+don't+hate+you. explain
```

### 只对指定关键字附近的文本进行分类

```
keywords [逗号分隔的列表——只分类与其接近的]
wordsBeforeKeywords [关键词之前的要分类的词（默认4个）]
wordsAfterKeywords [关键词之后的要分类的词（默认4个）]
```

### 将积极的(1到5)和消极的(-1到-5)情感强度分别分类

默认如此，除非选择二元、三元、单指标。1表示没有正面情绪，-1表示没有负面情绪。没有0的输出。

### 用三元分类

trinary（报告正-负-中性分类）

如3 -1 1（+ve -ve 三元）

### 用二元分类

binary（报告正-负分类）

如3 -1 1（+ve -ve 二元）

### 用单指标

scale（报告单个-4到+4的分类）

如3 -4 -1（+ve -ve 单指标）

### 语言数据文件夹的位置

sentidata [SentiStrength数据文件夹路径（斜线结束，无空格）]

### 情感词权重的位置

EmotionLookupTable [文件路径（默认：EmotionLookupTable.txt 或 SentimentLookupTable.txt）]

### 输出文件夹的位置

outputFolder [输出文件夹路径（默认：输入文件夹）]

### 输出的文件名扩展名

Resultsextension [输出文件扩展名（默认_out.txt）]

### 分类算法参数

这些选项改变了情感分析算法的工作方式

* alwaysSplitWordsAtApostrophes     （遇到撇号时将单词分开）
* noBoosters     （忽略情感增强词，例如very）
* noNegatingPositiveFlipsEmotion     (不要用否定的词来翻转+ve的词)
* noNegatingNegativeNeutralisesEmotion     (不要用否定词来无效化-ve词汇)
* negatedWordStrengthMultiplier     (否定时的强度倍增器（默认为0.5）)
* maxWordsBeforeSentimentToNegate     (否定词和情感词之间的最大单词数（默认0）)
* noIdioms (忽略idiom列表)
* questionsReduceNeg (减少疑问句中的-ve情感)
* noEmoticons (忽略emotion列表)
* exclamations2     (若不是-ve句子则算感叹号+2)
* mood [-1,0,1]     (对中性强调的解释，-1表示中性强调解释为-ve，1表示解释为+ve，0表示忽略)
* noMultiplePosWords     (不要让多个+ve词增加+ve情感)
* noMultipleNegWords     (不要让多个-ve词增加-ve情感)
* noIgnoreBoosterWordsAfterNegatives     (否定词后不要忽略增强词)
* noDictionary (不要试图用字典来纠正拼写错误（如，把不认识的单词中的重复字母删除成已知的单词）)
* noDeleteExtraDuplicateLetters     (不要删除单词中多余的重复字母) [这个选项不检查新单词是否合法，与上面的选项相反] 
* illegalDoubleLettersInWordMiddle     [字母在单词中间不会重复] 这是一个字符列表，其中的字符不会连续出现两次。对于英语，使用以下列表(默认):ahijkquvxyz永远不要把w包含在这个列表中，因为它经常出现在www中
* illegalDoubleLettersAtWordEnd     [字母永远不会在单词末尾重复]这是一个字符列表，从不在单词末尾连续出现两次。对于英语，使用以下列表(默认值):achijkmnpqruvwxyz
* noMultipleLetters     (不要在一个单词中使用多的字母来提升情感)

## 额外注意事项

### 语言问题

如果使用的语言的字符集不是标准ASCII集合，那么请保存为UTF8格式，并使用UTF8选项让SentiStrength以UTF8的形式读取输入文件

### 长文本

SentiStrength是专为短文本设计的，但可以使用以下选项用于较长文本的极性检测。。在这种模式下，总积极情绪会被计算出来，并与总消极情绪进行比较。如果总积极情绪大于1.5*总消极情绪，则分类为积极的，否则为消极的。为什么是1.5 ?因为消极比积极更罕见，所以更突出。

```
java -jar SentiStrength.jar sentidata C:/SentiStrength_Data/ text I+hate+frogs+but+love+dogs.+Do+You+like. sentenceCombineTot paragraphCombineTot trinary
```

如果你喜欢1.5以外的乘数，那么用negvmultiplier选项设置它。

```
java -jar SentiStrength.jar sentidata C:/SentiStrength_Data/ text I+hate+frogs+but+love+dogs.+Do+You+like. sentenceCombineTot paragraphCombineTot trinary negativeMultiplier 1
```

## 机器学习评估

train（通过对文件中的结果进行训练term strength来评估SentiStrength），需要一个500+人工分类文本的输入文件，如

```
java -jar SentiStrength.jar train input C:\1041MySpace.txt
```

使用机器学习方法和10折交叉验证来优化情感词典。这相当于在随机90%的数据上使用命令optimise，然后评估剩余10%的结果，并对10%数据的剩余9段重复9次以上。报告的准确性结果是10次尝试的平均值。这估计了使用optimise命令改进情感词典所提高的准确率。

输出为两个文件。以_out.txt结尾的文件报告各种准确性统计，例如，数字和比例正确；SentiStrength和人类编码值之间的相关性。以 _out_termStrVars.txt结尾的文件报告了每个折叠中情感词典的变化。这两个文件还报告了用于情感算法和机器学习的参数。

### 评估选项

* *all* 测试上述分类算法参数中列出的所有选项变化，而不是使用默认选项
* *tot* 通过正确分类的数量而不是分类差异的总和来优化
* *iterations* [十折迭代数，默认1] 设置进行训练和评估的次数。建议值为30，以帮助平均两次运行之间的差异
* *minImprovement* [最小额外正确类，默认2] 设置在训练阶段调整术语权重所需的最小额外正确分类数
* *multi* [重复术语强度优化以改变情感权重（默认1）] 这是一种super-optimisation。不是一次优化，而是从初始值开始多次优化词项权重，然后取这些权重的平均值进行优化，并用作最终优化的词项强度。这在理论上应该比优化一次给出更好的值

举例：

```
java -jar SentiStrength.jar multi 8 input C:\1041MySpace.txt iterations 2
```

需要一个输入文件，它是一个文本列表，包含积极(1-5)和消极(1-5)情绪的人工分类值。文件的每一行都应该是这样的格式：正 <tab> 负 <tab> 文本

怎么run这个test

```
java -jar SentiStrength.jar input [输入文件的路径] iterations [迭代数]
```

## 命令列表

### =要分类的数据来源=

* text [要处理的文本] O
* input [文件路径] (文件内每一行被单独分类，+ve 第一列，-ve 第二列) O
  * annotateCol [col # 1..] (分类在col的文本，结果生成于行末) O
  * textCol, idCol [col # 1..] (分类在col的文本，结果和ID生成于新文件) O

* inputFolder [foldername] (所有文件均会被标注) O
  * outputFolder [输出文件夹路径(默认为输入文件夹)] X
  * resultsExtension [文件扩展名(默认为_out.txt)] O
  * statsFile [filename] (评估，拷贝eval. stats到文件) ?
  * fileSubstring [text] (字符串必须在文件中出现注释) O
  * Ok to overwrite files [overwrite] O

* listen [端口号] X
* cmd (等待stdin输入，写入stdout，终止输入)@end X
* stdin (从stdin输入读取，写入stdout，stdin完成时终止) X
* wait (只是初始化；允许调用公共字符串computeSentimentScores) X

### =语言数据源=

* sentidata [SentiStrength数据文件夹(以斜杠结尾，没有空格] O

### =选项=

* keywords [逗号分隔的列表] O
* wordsBeforeKeywords [在关键字之前分类的单词(默认4)] O
* wordsAfterKeywords [在关键字之后分类的单词(默认4)] O
* trinary (报告正-负-中性分类) O
* binary (报告正-负分类) O
* scale (报告单指标-4到+4的分类) O
* emotionLookupTable [文件名(默认:EmotionLookupTable.txt)] X
* additionalFile [filename] (领域特定术语和评估) X
* lemmaFile [filename] (单词标签词表词形) X

### =分类算法参数= 全要

* noBoosters (忽略情感增强词)
* noNegators (不使用否定词(例如，not)翻转情感)
* noNegatingPositiveFlipsEmotion (不使用否动词翻转+ve词)
* bgNegatingNegativeNeutralisesEmotion (否动词不无效化-ve词)
* negatedWordStrengthMultiplier (被否定时的强度倍率(默认0.5))
* negatingWordsOccurAfterSentiment (否定词之前出现的的情感)
* maxWordsAfterSentimentToNegate (在否定情感后的最大单词数(默认0))
* negatingWordsDontOccurBeforeSentiment (不否定否定词之后的情感)
* maxWordsBeforeSentimentToNegate (在否定情感前的最大单词数(默认0))
* noIdioms (忽略idiom列表)
* questionsReduceNeg (疑问句-ve情绪减弱)
* noEmoticons (忽略emoticon列表)
* exclamations2 (有！的句子如果不是中性则算+2)
* minPunctuationWithExclamation (增强术语强度的最小带有!的标点)
* mood [-1,0,1] (默认1，-1 且具有中性强调是消极，1是积极)
* noMultiplePosWords (多个+ve的单词不会增加积极情绪)
* noMultipleNegWords (多个-ve的单词不会增加消极情绪)
* noIgnoreBoosterWordsAfterNegatives (不忽略否定词后的增强词)
* noDictionary (不用字典纠正拼写)
* noMultipleLetters (不用单词中的重复字母增强情绪)
* noDeleteExtraDuplicateLetters (不要删除单词中多余的重复字母)
* illegalDoubleLettersInWordMiddle [单词中间的字母永远不会重复]
  * 英语的默认值: ahijkquvxyz (指定没有空格的列表)
* illegalDoubleLettersAtWordEnd [单词结尾的字母永远不会重复]
  * 英语的默认值: achijkmnpqruvwxyz (指定列表不带空格)
* sentenceCombineAv (每个句子中术语的平均情感强度)
* sentenceCombineTot (每个句子中术语的情感强度之和)
* paragraphCombineAv (每个文本中句子的平均情感强度)

* paragraphCombineTot (每个文本中句子的情感强度之和)
  * 上述4个选项的默认值为最大值，不是总数或平均
* negativeMultiplier [负总强度极性乘数，默认1.5]
* capitalsBoostTermSentiment (大写的情感词更强烈)
* alwaysSplitWordsAtApostrophes
* MinSentencePosForQuotesIrony [integer] 在+ve句子中的引用表示反讽
* MinSentencePosForPunctuationIrony [integer] +ve 以!!+结束表示反讽
* MinSentencePosForTermsIrony [integer] +ve的反讽术语表示反讽
* MinSentencePosForAllIrony [integer] 上述所有反问术语
* lang [ISO-639 lower-case two-letter langauge code] 设置处理语言

### =输入输出=

* explain (解释分类后的结果)
* echo (echo结果后的原始文本[用于管道过程])
* UTF8 (强制所有处理为UTF-8格式)
* urlencoded (输入和输出文本为URL编码) X

### =进阶-机器学习=

* termWeights (分类不好的文本中的列表术语；必须指定inputFile)

* optimise [输入文件路径]

* train (通过在文件中的结果上训练词强度来评估SentiStrength))

* all (测试所有选项变化而不是使用默认值)

* numCorrect (通过纠正-而不是总分类差异进行优化)

* iterations [十折的次数] (default 1)

* minImprovement [更改情感权重的最小精度提高(默认值1)]

* multi [重复词强度优化以更改情感权重(默认值1)]

* skipheaderline [忽略第一行输入File(默认值为true)]

* noheaderline [不要忽略输入文件的第一行(默认值为false)]

   

第一类：text

第二类：单个文件