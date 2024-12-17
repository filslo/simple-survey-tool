import axios from 'axios';
import config from '../config';

export default class APIService {

    constructor() {

        this.client = axios.create(
            {
                baseURL: `${config.apiUrl}`,
            }
        );


        this.client.interceptors.request.use(function (config) {
            return config
        }, function (error) {
            return Promise.reject(error);
        });

        this.client.interceptors.response.use(function (response) {
            return response;
        }, function (error) {
            return Promise.reject(error);
        });

    }

    async fetchAllSurveys() {
        const url = '/surveys';
        return await this.client.get(url)
            .then(response => {
                console.debug('Fetched all surveys');

                if (response.data && response.data.length > 0) {
                    return response.data;
                } else {
                    return [];
                }
            })
            .catch(error => {
                console.error('Error fetching surveys:', error);
                return [];
            });
    }

    fetchSurveyById(surveyId) {
        if (surveyId !== undefined) {
            const url = `/surveys/${surveyId}`;
            return this.client.get(url)
                .then(response => {
                    console.debug(`Fetching survey: ${surveyId}`);

                    if (response.data) {
                        return response.data;
                    } else {
                        console.warn(`Survey not found: ${surveyId}`);
                        return undefined;
                    }
                })
                .catch(error => {
                    console.error('Error fetching Survey:', error);
                    return [];
                });
        }
    }

}
