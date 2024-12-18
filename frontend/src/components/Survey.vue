<template>
    <div v-if="survey">
        <h1>{{ survey.name }}</h1>
        <form @submit.prevent="submitAnswers(this.survey)" >

            <ul>
                <li v-for="question in survey.questions"
                    :key="question.id"
                    class="no-bullets">
                    {{ question.text }}
                    <p>Your answer:
                        <star-rating
                            v-model:rating="question.rating"
                            :inline="true"
                            :star-size="20"
                        />
                    </p>
                </li>
            </ul>

            <button type="submit" v-if="!savingSuccessful">Submit</button>
            <div class="success" v-if="savingSuccessful">
                Answers successfully submitted
            </div>

        </form>

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
            survey: {},
            apiService: new APIService(),
            savingSuccessful: false,
        };
    },

    watch: {
        rating(newRating, oldRating) {
            if (oldRating !== newRating) {
                this.setRating(newRating);
            }
        }
    },
    mounted() {

        const id = this.$route.params.id;
        this.fetchSurvey(id);

    },

    methods: {
        fetchSurvey(surveyId) {

            if (surveyId !== undefined) {
                this.apiService.fetchSurveyById(surveyId)
                    .then(
                        survey => {
                            survey.questions.forEach(
                                question => {
                                    question.rating = 0;
                                }
                            );
                            this.survey = survey;
                        }
                    );
            }
        },

        submitAnswers(survey) {
            let ratings = survey.questions.map(
                question => {
                    return { id: Number(question.id), rating: question.rating }
                }
            );
            this.apiService.submitAnswers(survey.id, ratings)
                .then(
                    this.savingSuccessful = true
                );
        },
    },
};
</script>
