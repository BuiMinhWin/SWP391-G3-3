import axios from "axios";

const REST_API_BASE_URL = "http://222.255.238.187:8080/api/accounts";

export const listAccount = () => {
  return axios.get(REST_API_BASE_URL);
};

export const createAccount = (account) => {
  return axios.post(REST_API_BASE_URL, account);
};

export const getAccount = (accountId) => {
  return axios.get(REST_API_BASE_URL + '/' + accountId);
};

export const updateAccount = (accountId, account) => {
  return axios.put(REST_API_BASE_URL + '/' + accountId, account);
};

export const deleteAccount = (accountId) => {
  return axios.delete(REST_API_BASE_URL + '/' + accountId);
};

export const loginAccount = (loginData) => {
  return axios.post(REST_API_BASE_URL + '/login', loginData, {
    headers: { 'Content-Type': 'application/json' },
    withCredentials: true,
  });
};
