<template>
    <div class="bd">
        <div class="background"></div>
        <div class="container">
            <header class="blog-header py-3">
                <div class="row flex-nowrap justify-content-between align-items-center">
                    <!--<div class="col-4 d-flex justify-content-end align-items-center" />-->
                    <div class="text-center" style="margin: 0 auto">
                        <h1 class="cool-text neon" style="margin: 1em 0 1em 0">SentiStrength Tool</h1>
                    </div>

                    <a class="col-1 button" href="/"
                       style="color: #ffffff; text-decoration: none; font-size: 1.1em; float: right;">
                        <p style="margin: 0 0 0 0.3em;">返回首页</p>
                    </a>
                </div>
            </header>

            <div class="api jumbotron p-3 p-md-5 text-black rounded back-white" style="height: 320px;">
                <h3 class="md-3">输入文本来源选择</h3>
                <el-radio v-model="inputType" label="text">Quick start（Text）</el-radio>
                <el-radio v-model="inputType" label="file">File</el-radio>
                <el-radio v-model="inputType" label="fileFolder">FileFolder</el-radio>

                <p></p>

                <!-- 输入方法API -->
                <div v-show="inputType==='text'">
                    <TextSentiAPI></TextSentiAPI>
                </div>
                <div v-show="inputType==='file'">
                    <FileSentiAPI></FileSentiAPI>
                </div>
                <div v-show="inputType==='fileFolder'">
                    <FolderSentiAPI></FolderSentiAPI>
                </div>
            </div>

            <!-- 翻译API -->
            <div class="jumbotron p-3 p-md-5 text-black rounded back-dark">
                <h3 class="md-3" style="color: #fff">1.翻译API</h3>
                <TranslationAPI :get_cmd='parseCmd'/>
            </div>


            <!-- 2.选项设置 -->
            <div class="jumbotron p-3 p-md-5 text-black rounded back-white">
                <h3 class="md-3">2.选项设置</h3>

                <div class="input-group mb-3">
                    <div class="input-group-text">
                        <div class="checkbox">
                            <label><input class="form-check-input" type="checkbox" v-model="setKeywards" value="keywords" @change="showKeywords = !showKeywords">是否设定关键词</label>
                        </div>
                    </div>
                    <input id="kwds" type="text" class="form-control" v-model="keywards" placeholder="[逗号分隔的列表]" v-if=showKeywords>
                </div>

                <div class="input-group mb-3" v-if=showKeywords>
                    <div class="input-group-text">
                        <div class="checkbox">
                            <label><input class="form-check-input" type="checkbox" v-model="setWordsBeforeKeywords" @change="showWordsBeforeKeywords = !showWordsBeforeKeywords" value="keywords">设定在关键字之前分类的单词数(默认为4)</label>
                        </div>
                    </div>
                    <input type="text" class="form-control" v-model="wordsBeforeKeywords" placeholder="大于等于0的整数" v-if=showWordsBeforeKeywords>
                </div>

                <div class="input-group mb-3" v-if=showKeywords>
                    <div class="input-group-text">
                        <div class="checkbox">
                            <label><input class="form-check-input" type="checkbox" v-model="setWordsAfterKeywords" @change="showWordsAfterKeywords = !showWordsAfterKeywords" value="keywords">设定在关键字之前分类的单词数(默认为4)</label>
                        </div>
                    </div>
                    <input type="text" class="form-control" v-model="wordsAfterKeywords" placeholder="大于等于0的整数" v-if=showWordsAfterKeywords>
                </div>

                <label for="classifyMethod">分类方法</label>
                <select class="custom-select d-block w-100" id="classifyMethod" v-model="classifyMethod">
                    <option value="">Dual &ensp;&ensp; 默认分类结果为:积极情绪(1~5) 消极情绪(-1~-5)</option>
                    <option value="binary">Binary &ensp;&ensp; 在默认分类结果基础上计算出整体情绪倾向(-1为消极,
                        1为积极)
                    </option>
                    <option value="trinary">Trinary &ensp;&ensp; 在默认分类结果基础上计算出整体情绪倾向(-1为消极, 1为积极,
                        0为中性)
                    </option>
                    <option value="scale">Scale &ensp;&ensp; 在默认分类结果基础上计算出更加细化的整体情绪倾向(-4~4)
                    </option>
                </select>

            </div>

            <!-- 3.分类算法参数设置 -->
            <div class="jumbotron p-3 p-md-5 text-white rounded back-dark" style="max-height: 40em; overflow-x: hidden; overflow-y: auto;">
                <h3 class="md-3">3.分类算法参数设置</h3>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="noBoosters" id="noBoosters" v-model="noBoosters">
                    <label class="form-check-label" for="noBoosters">
                        忽略情感增强词
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="noNegators" id="noNegators" v-model="noNegators">
                    <label class="form-check-label" for="noNegators">
                        不使用否定词(例如,not)翻转情感(功能暂未实现)
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="noNegatingPositiveFlipsEmotion"
                           id="noNegatingPositiveFlipsEmotion" v-model="noNegatingPositiveFlipsEmotion">
                    <label class="form-check-label" for="noNegatingPositiveFlipsEmotion">
                        不使用否动词翻转+ve词
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="bgNegatingNegativeNeutralisesEmotion"
                           id="bgNegatingNegativeNeutralisesEmotion" v-model="bgNegatingNegativeNeutralisesEmotion">
                    <label class="form-check-label" for="bgNegatingNegativeNeutralisesEmotion">
                        否动词不无效化-ve词
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="negatedWordStrengthMultiplier"
                           id="negatedWordStrengthMultiplier" v-model="setNegatedWordStrengthMultiplier"
                           @change="showNegatedWordStrengthMultiplier = !showNegatedWordStrengthMultiplier">
                    <label class="form-check-label" for="negatedWordStrengthMultiplier">
                        修改被否定时的强度倍率(默认为0.5)
                    </label>
                    <input type="text" class="form-check" v-model="negatedWordStrengthMultiplier" placeholder="0~1"
                           v-if=showNegatedWordStrengthMultiplier>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="negatingWordsOccurAfterSentiment"
                           id="negatingWordsOccurAfterSentiment" v-model="negatingWordsOccurAfterSentiment"
                           @change="setNegatingWordsOccurAfterSentiment = !setNegatingWordsOccurAfterSentiment">
                    <label class="form-check-label" for="negatingWordsOccurAfterSentiment">
                        是否允许否定词后置(如"I am happy not")
                    </label>
                </div>
                <div class="form-check" v-if=setNegatingWordsOccurAfterSentiment>
                    <input class="form-check-input" type="checkbox" value="maxWordsAfterSentimentToNegate"
                           id="maxWordsAfterSentimentToNegate" v-model="setMaxWordsAfterSentimentToNegate"
                           @change="showMaxWordsAfterSentimentToNegate = !showMaxWordsAfterSentimentToNegate">
                    <label class="form-check-label" for="maxWordsAfterSentimentToNegate">
                        是否修改否定词在情感词之后,二者之间的的最大值(默认为0)
                    </label>
                    <input type="text" class="form-check" v-model="maxWordsAfterSentimentToNegate"
                           placeholder="大于等于0的整数" v-if=showMaxWordsAfterSentimentToNegate>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="negatingWordsDontOccurBeforeSentiment"
                           id="negatingWordsDontOccurBeforeSentiment" v-model="negatingWordsDontOccurBeforeSentiment"
                           @change="setNegatingWordsDontOccurBeforeSentiment = !setNegatingWordsDontOccurBeforeSentiment">
                    <label class="form-check-label" for="negatingWordsDontOccurBeforeSentiment">
                        是否不允许否定词前置(如"not I am happy")
                    </label>
                </div>
                <div class="form-check" v-if=setNegatingWordsDontOccurBeforeSentiment>
                    <input class="form-check-input" type="checkbox" value="maxWordsBeforeSentimentToNegate"
                           id="maxWordsBeforeSentimentToNegate" v-model="setMaxWordsBeforeSentimentToNegate"
                           @change="showMaxWordsBeforeSentimentToNegate = !showMaxWordsBeforeSentimentToNegate">
                    <label class="form-check-label" for="maxWordsBeforeSentimentToNegate">
                        是否修改否定词在情感词之前,二者之间的的最大值(默认为0)
                    </label>
                    <input type="text" class="form-check" v-model="maxWordsBeforeSentimentToNegate"
                           placeholder="大于等于0的整数" v-if=showMaxWordsBeforeSentimentToNegate>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="noIdioms" id="noIdioms" v-model="noIdioms">
                    <label class="form-check-label" for="noIdioms">
                        忽略idiom列表
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="questionsReduceNeg" id="questionsReduceNeg"
                           v-model="questionsReduceNeg">
                    <label class="form-check-label" for="questionsReduceNeg">
                        疑问句的负面情绪减弱
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="noEmoticons" id="noEmoticons"
                           v-model="noEmoticons">
                    <label class="form-check-label" for="noEmoticons">
                        忽略emoticon列表
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="exclamations2" id="exclamations2"
                           v-model="exclamations2">
                    <label class="form-check-label" for="exclamations2">
                        有！的句子如果不是中性则情感强度+2
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="minPunctuationWithExclamation"
                           id="minPunctuationWithExclamation" v-model="setMinPunctuationWithExclamation"
                           @change="showMinPunctuationWithExclamation = !showMinPunctuationWithExclamation">
                    <label class="form-check-label" for="minPunctuationWithExclamation">
                        设置最少带有几个!才会增强情绪强度(默认为1)
                    </label>
                    <input type="text" class="form-check" v-model="minPunctuationWithExclamation" placeholder="正整数"
                           v-if=showMinPunctuationWithExclamation>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="mood" id="mood" v-model="setMood"
                           @change="showMood = !showMood">
                    <label class="form-check-label" for="mood">
                        对中性强调的解释，-1表示中性强调解释为-ve,1表示解释为+ve,0表示忽略
                    </label>
                    <input type="text" class="form-check" v-model="mood" placeholder="-1或0或1" v-if=showMood>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="noMultiplePosWords" id="noMultiplePosWords"
                           v-model="noMultiplePosWords">
                    <label class="form-check-label" for="noMultiplePosWords">
                        多个积极情绪词不会增强整体的积极情绪
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="noMultipleNegWords" id="noMultipleNegWords"
                           v-model="noMultipleNegWords">
                    <label class="form-check-label" for="noMultipleNegWords">
                        多个消极情绪词不会增强整体的消极情绪
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="noIgnoreBoosterWordsAfterNegatives"
                           id="noIgnoreBoosterWordsAfterNegatives" v-model="noIgnoreBoosterWordsAfterNegatives">
                    <label class="form-check-label" for="noIgnoreBoosterWordsAfterNegatives">
                        不忽略否定词后的增强词
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="noDictionary" id="noDictionary"
                           v-model="noDictionary">
                    <label class="form-check-label" for="noDictionary">
                        不用字典纠正拼写
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="noMultipleLetters" id="noMultipleLetters"
                           v-model="noMultipleLetters">
                    <label class="form-check-label" for="noMultipleLetters">
                        不用单词中的重复字母增强情绪
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="noMultipleNegWords" id="noMultipleNegWords"
                           v-model="noMultipleNegWords">
                    <label class="form-check-label" for="noMultipleNegWords">
                        不要删除单词中多余的重复字母
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="illegalDoubleLettersInWordMiddle"
                           id="illegalDoubleLettersInWordMiddle" v-model="setIllegalDoubleLettersInWordMiddle"
                           @change="showIllegalDoubleLettersInWordMiddle = !showIllegalDoubleLettersInWordMiddle">
                    <label class="form-check-label" for="illegalDoubleLettersInWordMiddle">
                        设置单词中间的哪些字母永远不会重复,默认值: ahijkquvxyz (指定没有空格的列表)
                    </label>
                    <input type="text" class="form-check" v-model="illegalDoubleLettersInWordMiddle"
                           placeholder="ahijkquvxyz" v-if=showIllegalDoubleLettersInWordMiddle>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="illegalDoubleLettersAtWordEnd"
                           id="illegalDoubleLettersAtWordEnd" v-model="setIllegalDoubleLettersAtWordEnd"
                           @change="showIllegalDoubleLettersAtWordEnd = !showIllegalDoubleLettersAtWordEnd">
                    <label class="form-check-label" for="illegalDoubleLettersAtWordEnd">
                        设置单词结尾的哪些字母永远不会重复,默认值: achijkmnpqruvwxyz (指定列表不带空格)
                    </label>
                    <input type="text" class="form-check" v-model="illegalDoubleLettersAtWordEnd"
                           placeholder="achijkmnpqruvwxyz" v-if=showIllegalDoubleLettersAtWordEnd>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="sentenceCombineAv" id="sentenceCombineAv"
                           v-model="sentenceCombineAv">
                    <label class="form-check-label" for="sentenceCombineAv">
                        句子的输出结果改为句子中术语的平均情感强度(默认为最大值)
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="sentenceCombineTot" id="sentenceCombineTot"
                           v-model="sentenceCombineTot">
                    <label class="form-check-label" for="sentenceCombineTot">
                        句子的输出结果改为句子中术语的情感强度之和(默认为最大值)
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="paragraphCombineAv" id="paragraphCombineAv"
                           v-model="paragraphCombineAv">
                    <label class="form-check-label" for="paragraphCombineAv">
                        段落的输出结果改为段落中句子的平均情感强度(默认为最大值)
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="paragraphCombineTot" id="paragraphCombineTot"
                           v-model="paragraphCombineTot">
                    <label class="form-check-label" for="paragraphCombineTot">
                        段落的输出结果改为段落中句子的情感强度之和(默认为最大值)
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="negativeMultiplier" id="negativeMultiplier"
                           v-model="setNegativeMultiplier" @change="showNegativeMultiplier = !showNegativeMultiplier">
                    <label class="form-check-label" for="negativeMultiplier">
                        负总强度极性乘数,默认1.5
                    </label>
                    <input type="text" class="form-check" v-model="negativeMultiplier" placeholder="1.5"
                           v-if=showNegativeMultiplier>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="capitalsBoostTermSentiment"
                           id="capitalsBoostTermSentiment" v-model="capitalsBoostTermSentiment">
                    <label class="form-check-label" for="capitalsBoostTermSentiment">
                        大写的情感词更强烈
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="alwaysSplitWordsAtApostrophes"
                           id="alwaysSplitWordsAtApostrophes" v-model="alwaysSplitWordsAtApostrophes">
                    <label class="form-check-label" for="alwaysSplitWordsAtApostrophes">
                        遇到撇号时将单词分开
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="MinSentencePosForQuotesIrony"
                           id="MinSentencePosForQuotesIrony" v-model="setMinSentencePosForQuotesIrony"
                           @change="showMinSentencePosForQuotesIrony = !showMinSentencePosForQuotesIrony">
                    <label class="form-check-label" for="MinSentencePosForQuotesIrony">
                        在积极情绪句子中的反引号表示反讽
                    </label>
                    <input type="text" class="form-check" v-model="MinSentencePosForQuotesIrony" placeholder="整数"
                           v-if=showMinSentencePosForQuotesIrony>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="MinSentencePosForPunctuationIrony"
                           id="MinSentencePosForPunctuationIrony" v-model="setMinSentencePosForPunctuationIrony"
                           @change="showMinSentencePosForPunctuationIrony = !showMinSentencePosForPunctuationIrony">
                    <label class="form-check-label" for="MinSentencePosForPunctuationIrony">
                        在积极情绪句子中以!!+结束表示反讽
                    </label>
                    <input type="text" class="form-check" v-model="MinSentencePosForPunctuationIrony" placeholder="整数"
                           v-if=showMinSentencePosForPunctuationIrony>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="MinSentencePosForTermsIrony"
                           id="MinSentencePosForTermsIrony" v-model="setMinSentencePosForTermsIrony"
                           @change="showMinSentencePosForTermsIrony = !showMinSentencePosForTermsIrony">
                    <label class="form-check-label" for="MinSentencePosForTermsIrony">
                        在积极情绪句子中的反讽术语表示反讽
                    </label>
                    <input type="text" class="form-check" v-model="MinSentencePosForTermsIrony" placeholder="整数"
                           v-if=showMinSentencePosForTermsIrony>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="MinSentencePosForAllIrony"
                           id="MinSentencePosForAllIrony" v-model="setMinSentencePosForAllIrony"
                           @change="showMinSentencePosForAllIrony = !showMinSentencePosForAllIrony">
                    <label class="form-check-label" for="MinSentencePosForAllIrony">
                        设定上述所有反问术语均
                    </label>
                    <input type="text" class="form-check" v-model="MinSentencePosForAllIrony" placeholder="整数"
                           v-if=showMinSentencePosForAllIrony>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="lang" id="lang" v-model="setlang"
                           @change="showLang = !showLang">
                    <label class="form-check-label" for="lang">
                        设置处理语言
                    </label>
                    <input type="text" class="form-check" v-model="lang"
                           placeholder="ISO-639 lower-case two-letter langauge code" v-if=showLang>
                </div>
            </div>

            <!-- 4.输入输出设置 -->
            <div class="jumbotron p-3 p-md-5 text-black rounded back-white">
                <h3 class="md-3">4.输入输出设置</h3>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="explain" id="explain" v-model="explain">
                    <label class="form-check-label" for="explain">
                        解释分类后的结果
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="echo" id="echo" v-model="echo">
                    <label class="form-check-label" for="echo">
                        将结果后的原始文本回显[用于管道过程]
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="UTF8" id="UTF8" v-model="UTF8">
                    <label class="form-check-label" for="UTF8">
                        强制所有处理为UTF-8格式
                    </label>
                </div>
            </div>
        </div>
    </div>
    <i style="color: #FFFFFF;font-size: 16px;left: 50%;position: absolute;transform: translateX(-50%); padding: 5em 0 8em 0">Copyright
        &#169; 2023 | RainbowSix NJUSE | Base on Nginx and Vue3</i>
</template>

<style>
.back-dark {
    background-color: rgba(105, 105, 105, 80%);
    transition: all 0.2s linear;
}

.back-dark:hover {
    background-color: rgba(0, 0, 0, 80%);
    transition-timing-function: ease-in-out;
}

.back-white {
    background-color: rgba(255, 255, 255, 60%);
    transition: all 0.2s linear;
}

.back-white:hover {
    background-color: rgba(255, 255, 255, 80%);
    transition-timing-function: ease-in-out;
}

.bd {
    overflow-y: auto;
}

.background {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-image: url("@/assets/nightCity.jpg");
    background-size: cover;
    background-attachment: fixed;
    opacity: 1;
    filter: blur(8px);
    z-index: -1;
}
</style>

<script>
import TranslationAPI from "@/components/SentiAPI_Components/TranslationAPI.vue";
import FileSentiAPI from "@/components/SentiAPI_Components/FileSentiAPI.vue";
import FolderSentiAPI from "@/components/SentiAPI_Components/FolderSentiAPI.vue";
import TextSentiAPI from "@/components/SentiAPI_Components/TextSentiAPI.vue";

export default {
    name: 'SentiAPI',
    components: {
        TranslationAPI,
        FileSentiAPI,
        FolderSentiAPI,
        TextSentiAPI,
    },
    data() {
        return {
            showKeywords: false,
            showWordsBeforeKeywords: false,
            showWordsAfterKeywords: false,
            showNegatedWordStrengthMultiplier: false,
            setNegatingWordsOccurAfterSentiment: false,
            showMaxWordsAfterSentimentToNegate: false,
            setNegatingWordsDontOccurBeforeSentiment: true,
            showMaxWordsBeforeSentimentToNegate: false,
            showMinPunctuationWithExclamation: false,
            showMood: false,
            showIllegalDoubleLettersInWordMiddle: false,
            showIllegalDoubleLettersAtWordEnd: false,
            showNegativeMultiplier: false,
            showMinSentencePosForQuotesIrony: false,
            showMinSentencePosForPunctuationIrony: false,
            showMinSentencePosForTermsIrony: false,
            showMinSentencePosForAllIrony: false,
            showLang: false,

            inputText: '',
            setKeywards: [],
            keywards: '',
            setWordsBeforeKeywords: [],
            wordsBeforeKeywords: '',
            setWordsAfterKeywords: [],
            wordsAfterKeywords: '',
            classifyMethod: '',
            noBoosters: [],
            noNegators: [],
            noNegatingPositiveFlipsEmotion: [],
            bgNegatingNegativeNeutralisesEmotion: [],
            setNegatedWordStrengthMultiplier: [],
            negatedWordStrengthMultiplier: '',
            negatingWordsOccurAfterSentiment: [],
            setMaxWordsAfterSentimentToNegate: [],
            maxWordsAfterSentimentToNegate: '',
            negatingWordsDontOccurBeforeSentiment: [],
            setMaxWordsBeforeSentimentToNegate: [],
            maxWordsBeforeSentimentToNegate: '',
            noIdioms: [],
            questionsReduceNeg: [],
            noEmoticons: [],
            exclamations2: [],
            setMinPunctuationWithExclamation: [],
            minPunctuationWithExclamation: '',
            setMood: [],
            mood: '',
            noMultiplePosWords: [],
            noMultipleNegWords: [],
            noIgnoreBoosterWordsAfterNegatives: [],
            noDictionary: [],
            noMultipleLetters: [],
            setIllegalDoubleLettersInWordMiddle: [],
            illegalDoubleLettersInWordMiddle: '',
            setIllegalDoubleLettersAtWordEnd: [],
            illegalDoubleLettersAtWordEnd: '',
            sentenceCombineAv: [],
            sentenceCombineTot: [],
            paragraphCombineAv: [],
            paragraphCombineTot: [],
            setNegativeMultiplier: [],
            negativeMultiplier: '',
            capitalsBoostTermSentiment: [],
            alwaysSplitWordsAtApostrophes: [],
            setMinSentencePosForQuotesIrony: [],
            MinSentencePosForQuotesIrony: '',
            setMinSentencePosForPunctuationIrony: [],
            MinSentencePosForPunctuationIrony: '',
            setMinSentencePosForTermsIrony: [],
            MinSentencePosForTermsIrony: '',
            setMinSentencePosForAllIrony: [],
            MinSentencePosForAllIrony: '',
            setlang: [],
            lang: [],
            explain: [],
            echo: [],
            UTF8: [],
            inputType: "text",
            sendText: '',
            responseText: '',
        };
    },
    methods: {

        parseSentText(a) {
            if (typeof a !== 'undefined' && a != null && a !== '') {
                this.sendText = (this.sendText !== '') ? (this.sendText + '\t' + a) : (a);
            }
        },

        parseCmd() {
            this.sendText = "";
            this.parseSentText(this.inputText);
            this.parseSentText(this.setKeywards[0]);
            this.parseSentText(this.keywards);
            this.parseSentText(this.setWordsBeforeKeywords[0]);
            this.parseSentText(this.wordsBeforeKeywords);
            this.parseSentText(this.setWordsAfterKeywords[0]);
            this.parseSentText(this.wordsAfterKeywords);
            this.parseSentText(this.classifyMethod);
            this.parseSentText(this.noBoosters[0]);
            this.parseSentText(this.noNegators[0]);
            this.parseSentText(this.noNegatingPositiveFlipsEmotion[0]);
            this.parseSentText(this.bgNegatingNegativeNeutralisesEmotion[0]);
            this.parseSentText(this.setNegatedWordStrengthMultiplier[0]);
            this.parseSentText(this.negatedWordStrengthMultiplier);
            this.parseSentText(this.negatingWordsOccurAfterSentiment[0]);
            this.parseSentText(this.setMaxWordsAfterSentimentToNegate[0]);
            this.parseSentText(this.maxWordsAfterSentimentToNegate);
            this.parseSentText(this.negatingWordsDontOccurBeforeSentiment[0]);
            this.parseSentText(this.setMaxWordsBeforeSentimentToNegate[0]);
            this.parseSentText(this.maxWordsBeforeSentimentToNegate);
            this.parseSentText(this.noIdioms[0]);
            this.parseSentText(this.questionsReduceNeg[0]);
            this.parseSentText(this.noEmoticons[0]);
            this.parseSentText(this.exclamations2[0]);
            this.parseSentText(this.setMinPunctuationWithExclamation[0]);
            this.parseSentText(this.minPunctuationWithExclamation);
            this.parseSentText(this.setMood[0]);
            this.parseSentText(this.mood);
            this.parseSentText(this.noMultiplePosWords[0]);
            this.parseSentText(this.noMultipleNegWords[0]);
            this.parseSentText(this.noIgnoreBoosterWordsAfterNegatives[0]);
            this.parseSentText(this.noDictionary[0]);
            this.parseSentText(this.noMultipleLetters[0]);
            this.parseSentText(this.setIllegalDoubleLettersInWordMiddle[0]);
            this.parseSentText(this.illegalDoubleLettersInWordMiddle);
            this.parseSentText(this.setIllegalDoubleLettersAtWordEnd[0]);
            this.parseSentText(this.illegalDoubleLettersAtWordEnd);
            this.parseSentText(this.sentenceCombineAv[0]);
            this.parseSentText(this.sentenceCombineTot[0]);
            this.parseSentText(this.paragraphCombineAv[0]);
            this.parseSentText(this.setNegativeMultiplier[0]);
            this.parseSentText(this.negativeMultiplier);
            this.parseSentText(this.capitalsBoostTermSentiment[0]);
            this.parseSentText(this.alwaysSplitWordsAtApostrophes[0]);
            this.parseSentText(this.setMinSentencePosForQuotesIrony[0]);
            this.parseSentText(this.MinSentencePosForQuotesIrony);
            this.parseSentText(this.setMinSentencePosForPunctuationIrony[0]);
            this.parseSentText(this.MinSentencePosForPunctuationIrony);
            this.parseSentText(this.setMinSentencePosForTermsIrony[0]);
            this.parseSentText(this.MinSentencePosForTermsIrony);
            this.parseSentText(this.setMinSentencePosForAllIrony[0]);
            this.parseSentText(this.setlang[0]);
            this.parseSentText(this.lang[0]);
            this.parseSentText(this.explain[0]);
            this.parseSentText(this.echo[0]);
            this.parseSentText(this.UTF8[0]);
            return this.sendText;
        },
    },
};
</script>