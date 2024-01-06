<template>
	<div>
		<div class="background-image"></div>
		<div class="main-content">
			<div class="text-wrapper">
				<h2 style="color: #F0F0F0">Welcome to our SE3 Project Navigator Page</h2>
				<h1 class="cool-text neon">This is Team RainbowSix.</h1>
			</div>
			<a @click="this.$router.push('/SentiAPI')" class="button-main" style="left: 27%">Click Here to SentiStrength Tool.</a>
			<a @click="this.$router.push('/AnalysisAPI')" class="button-main" style="left: 73%">Click Here to Senti-Analysis Tool.</a>
		</div>

		<div class="buttons-wrapper">
			<a href="/JavaDoc/index.html" class="button">JavaDoc</a>
			<a href="http://124.221.102.208/nacos" class="button">微服务-Nacos</a>
			<a href="http://124.221.102.208/controller/test" class="button">Controller-test</a>
			<a href="http://172.29.4.49/08" class="button">Repository-Gitlab</a>
			<a href="#" class="button">Contact Us</a>
		</div>

		<i style="color: #FFFFFF;font-size: 16px;bottom: 10%;left: 50%;position: absolute;transform: translateX(-50%);">Copyright &#169; 2023 | RainbowSix NJUSE | Base on Nginx and Vue3</i>
	</div>
</template>

<script>
export default {
	name: 'HomePage'
}
</script>

<style scoped>
/* 引入原来的 index.css 文件 */
@import "@/assets/index.css";
</style>