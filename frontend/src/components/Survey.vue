<template>
    <div  v-if="survey">
        <h1>{{ survey.name }}</h1>

        <ul>
            <li v-for="question in survey.questions"
                :key="question.id"
                class="no-bullets">
                {{ question.text }}
            </li>
        </ul>

    </div>
</template>

<script lang="ts">

import APIService from "../services/APIService.js";


export default {
    components: {},

    data() {
        return {
            survey: {},
        };
    },
    mounted() {

        const id = this.$route.params.id;
        this.fetchSurvey(id);

    },

    methods: {
        fetchSurvey(surveyId) {

            if (surveyId !== undefined) {
                const apiService = new APIService();
                apiService.fetchSurveyById(surveyId)
                    .then(
                        survey => {
                            this.survey = survey;
                        });
            }
        },
    },
};
</script>
