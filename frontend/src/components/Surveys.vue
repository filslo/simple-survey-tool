<template>
    <div>
        <h2>Surveys</h2>

        <ul>
            <li v-for="survey in surveys" :key="survey.id" class="no-bullets">
                <router-link :to="{ name: 'survey', params: { id: survey.id } }">
                    {{ survey.name }}
                </router-link>
            </li>
        </ul>

    </div>
</template>

<script lang="ts">

import APIService from "../services/APIService.js";


export default {
    components: {
    },
    data() {
        return {
            surveys: [],
        };
    },
    mounted() {

        const apiService = new APIService();
        apiService.fetchAllSurveys()
            .then(surveys => {
                this.surveys = surveys;
            });
    },
    methods: {},
};
</script>
