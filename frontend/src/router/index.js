import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../components/HomePage.vue';
import SentiAPI from '../components/SentiAPI.vue';
import AnalysisAPI from "@/components/AnalysisAPI.vue";

const routes = [
		{ path: '/', name: "HomePage", component: HomePage },
		{ path: '/SentiAPI', name: "SentiAPI", component: SentiAPI },
		{ path: '/AnalysisAPI', name: "AnalysisAPI", component: AnalysisAPI}]
const router = createRouter({
	history: createWebHistory(),
	routes
})

export default router;