<template>
	<el-upload
			class="upload-demo"
			ref="upload"
			action="http://124.221.102.208:8081/senti-strength/file"
			:data="{ 'para':optionList }"
			:limit="1"
			:on-exceed="handleExceed"
			:file-list="fileList"
			:auto-upload="false"
			:before-upload="beforeAvatarUpload"
			:http-request="httpRequest"
			:on-success="handleSuccess">
		<template #trigger>
			<el-button style="margin-left: 10px;" size="small" type="primary" @click="this.$refs.upload.clearFiles();">选取单个文件</el-button>

		</template>


	</el-upload>
	<el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">开始分析</el-button>


	<!--tip-->
	<p></p>
	<i>注：只能上传txt文件</i>
	<div @click="downFile"><i class="el-icon-document"></i>{{fileinfo.virtualPath}}</div>
	<br/>
	<br/>
	<br/>
</template>
<script>



import {ElMessageBox} from "element-plus";
import axios from "axios";

export default {
	data() {
		return {
			fileList: [],
			fileinfo:{
				virtualPath:"",
			},
			optionList: ""
		};
	},
	methods: {
		submitUpload() {
			this.$refs.upload.submit();
		},
		handleExceed(){
			return ElMessageBox.alert(
					`Limit 1 file`
			).then(
					() => true
			)
		},
		beforeAvatarUpload(file){
			const isTxt = file.type === 'text/plain';
			if(!isTxt){
				this.$message.error('上传文件格式只能是txt!')
			}
			return isTxt;
		},
		handleSuccess(result){
			this.fileinfo.virtualPath=result;
		},
		downFile(){
			var url = "http://124.221.102.208:8081/senti-strength/downloadFile?filePath="+this.fileinfo.virtualPath;
			window.open(url);
		},
		httpRequest(param) {
			let fd = new FormData();
			fd.append("file", param.file);
			this.optionList = "input\t"+this.$parent.parseCmd();
			fd.append("para", this.optionList);
			axios.post('http://124.221.102.208:8081/senti-strength/file', fd , {
				headers: {'Content-Type': 'multipart/form-data'},//定义内容格式,很重要
				timeout: 20000,
			}).then(response => {
				this.fileinfo.virtualPath=response.data;
			}).catch(err =>{console.log(err);
				})
		}
	}
}
</script>


