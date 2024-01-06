<template>
    <div style="z-index: 100;">
        <div>
            <input v-model="inputText" type="text" placeholder="Enter text to send to API" style="width: 100%;"/>
        </div>
        <p></p>
        <div>
            <el-button type="primary" size="small" @click="sendText">提交</el-button>
        </div>
        <p></p>
        <div>{{ responseText }}</div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    name: 'SentiAPI',
    data() {
        return {
            inputText: '',
            responseText: '',
            myOptionList:""
        };
    },
    methods: {
        sendText() {
            let fd = new FormData();
            this.myOptionList = this.$parent.parseCmd();
            console.log(this.myOptionList);
            fd.append("text", this.inputText.toString());
            fd.append("para", "text\t"+this.myOptionList);
            axios.post('http://124.221.102.208/controller/text', fd , {
                headers: {'Content-Type': 'multipart/form-data'},//定义内容格式,很重要
                timeout: 20000,
            }).then((res) =>
                {
                    this.responseText = res.data;
                    this.myOptionList = [];
                }).catch(err =>{console.log(err);
                    this.myOptionList=[];
                })
        },
    },
};
</script>
