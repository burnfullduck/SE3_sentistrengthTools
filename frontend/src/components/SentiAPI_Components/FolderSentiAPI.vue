<template>
	<el-upload
			class="upload-demo"
			action="http://124.221.102.208:8081/senti-strength/fileFolder"
			:data="{
        'para':optionList
        }"
			accept=""
			:auto-upload="false"
			:multiple="true"
			:file-list="fileList"
			:on-change="fileChangeHandle"
			:http-request="httpRequest"
			ref="uploadFile">
		<el-button style="margin-left: 10px;" size="small" type="primary" ref="folder" @click="this.$refs.uploadFile.clearFiles()">
			选取文件夹
			<i class="el-icon-folder-opened"></i>
		</el-button>
	</el-upload>
	<el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">开始分析</el-button>
	<p></p>
	<div class="form-check">
		<input class="form-check-input" type="checkbox" value="annotateCol" id="annotateCol" v-model="setAnnotateCol" @change="showAnnotateCol = !showAnnotateCol">
		<label class="form-check-label" for="annotateCol">
			设置需要分析的文本在第几列(默认为1)
		</label>
		<input type="text" class="form-check" v-model="annotateCol" placeholder="正整数" v-if=showAnnotateCol>
	</div>
	<div v-for="(file,index) in virtualPathList" :key="index" @click="downFile(file)"><i class="el-icon-document"></i>{{file}}</div>
	<br/>
	<br/>
	<br/>
</template>
<script>



import axios from "axios";

export default {
	data() {
		return {
			fileList:[],
			uploadList:[],
			virtualPathList:[],
			optionList: "",
			showAnnotateCol: false,
			setAnnotateCol:[],
			annotateCol:'1',
		}
	},
	mounted:function () {
		const upLoadEle = document.querySelectorAll('.el-upload__input');
		upLoadEle[1].webkitdirectory = true;
	},

	methods: {
		submitUpload() {
			this.$refs.uploadFile.submit();
		},
		fileChangeHandle(file, fileList) {
			this.fileList = fileList;
		},
		handleSuccess(result){
			console.log(result);
			this.virtualPathList=result.data;
			console.log(this.virtualPathList);
		},
		downFile(file){
			var url = "http://124.221.102.208:8081/senti-strength/downloadFile?filePath="+encodeURI(file);
			window.open(url);
		},
		httpRequest(param) {
			let fd = new FormData();
			this.uploadList.push(param.file);
			if(this.uploadList.length<this.fileList.length || this.uploadList.length === 0){
				return;
			}
			var i;
			for(i in this.uploadList){
				fd.append("files", this.uploadList[i]);
			}
			this.optionList="fileSubstring\ttxt\t";
			this.optionList += "annotateCol"+'\t' +this.annotateCol;
			this.optionList += "\tinputFolder\t"+this.$parent.parseCmd();
			fd.append("para", this.optionList);
			axios.post('http://124.221.102.208:8081/senti-strength/fileFolder', fd , {
				headers: {'Content-Type': 'multipart/form-data'},//定义内容格式,很重要
				timeout: 20000,
			}).then(response => {
				console.log(response);
				this.virtualPathList = response.data;
				this.optionList = [];
				this.uploadList = [];
			}).catch(err =>{console.log(err);
				this.uploadList = [];})
		}


	}

}
</script>
