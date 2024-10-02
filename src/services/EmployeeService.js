import axios from "axios";

const REST_API_BASE_URL = "http://localhost:8080/api/accounts";


export const listAccount = () => {
  return axios.get(REST_API_BASE_URL);
};


export const createAccount = (account) => {
  return axios.post(REST_API_BASE_URL, account);
};

export const getAccount = (accountId) => axios.get(REST_API_BASE_URL + '/' + accountId);

export const updateAccount =(accountId, account) => axios.put(REST_API_BASE_URL + '/' + accountId, account);

export const deleteAccount = (accountId) =>axios.delete(REST_API_BASE_URL+ '/' + accountId);

export const loginAccount = (loginData) => {
  return axios.post(REST_API_BASE_URL + '/login', loginData);
};
