<template>
    <div v-if="surveyResults">
        <h1>{{ surveyResults.surveyName }}</h1>
            <ul>
                <li v-for="questionResult in surveyResults.questionResults"
                    class="no-bullets">
                    {{ questionResult.questionText }}
                    <p v-for="answerResult in questionResult.answerResults">
                        <star-rating
                            v-model:rating="answerResult.rating"
                            :inline="true"
                            :star-size="20"
                            :read-only="true"
                        />: {{answerResult.count}}
                    </p>
                </li>
            </ul>

    </div>
</template>

<script lang="ts">
import StarRating from "vue-star-rating"

import APIService from "../services/APIService.js";


export default {
    components: {
        StarRating,
    },

    data() {
        return {
            surveyResults: {},
            apiService: new APIService(),
        };
    },

    mounted() {

        const id = this.$route.params.id;
        this.fetchSurveyResults(id);

    },

    methods: {
        fetchSurveyResults(surveyId: string) {

            if (surveyId !== undefined) {
                this.apiService.fetchSurveyResults(surveyId)
                    .then(
                        surveyResults => {
                            this.surveyResults = surveyResults;
                        }
                    );
            }
        },
    },
};
</script>
