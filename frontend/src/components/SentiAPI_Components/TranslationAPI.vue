<script setup>
import { ref, defineProps } from 'vue'
import axios from 'axios'

// reactive state
const text = ref('')
const props = defineProps({
	get_cmd :  Function ,
});
const responseText = ref('');
function sendText() {

	let fd = new FormData();
	let myOptionList = props.get_cmd();
	fd.append("text", text.value.toString());
	fd.append("para", "text\t" + myOptionList);
	console.log(myOptionList);
	axios.post('http://124.221.102.208/controller/text', fd, {
		headers: {'Content-Type': 'multipart/form-data'},//定义内容格式,很重要
		timeout: 20000,
	}).then((res) => {
		responseText.value = res.data;
	}).catch(err => {
		console.log(err);
	})
}

function translate() {
	const crypto = require('crypto');
	console.log(text);
	let api = 'https://fanyi-api.baidu.com/api/trans/vip/translate';
	let raw = '';
	let q = text.value;
	raw = raw + '?q=' + q;
	raw = raw + '&from=auto';
	raw = raw + '&to=en';
	let app_id = '20230405001629785';
	raw = raw + '&appid=' + app_id;
	let salt = '';
	for (let i = 0; i < 10; i++) {
		salt = salt + Math.round(Math.random() * 9);
	}
	console.log(salt);
	raw = raw + '&salt=' + salt;
	console.log(salt);
	api = api + raw;
	let secret_key = '12gScEzaWA3k5rMfHemT';
	let before_md5 = app_id + q + salt + secret_key;
	const hash = crypto.createHash('md5');
	hash.update(before_md5);
	let sign = hash.digest('hex');
	api = api + '&sign=' + sign;
	console.log(api);
	axios.get('http://124.221.102.208/controller/translate',{
    params:{
      url : api
    }
  }).then((response) => {
		let result = response.data.trans_result[0].dst;
		text.value = result;
		sendText();
		console.log(result);
	})
}
</script>

<template>
	<input v-model="text" placeholder="请输入需要翻译的文本:" style="width: 100%">
	<br/>
	<button @click="translate">翻译并分析</button>
	<div style="color: whitesmoke">{{ responseText }}</div>
	<br/>
</template>

<style scoped></style>