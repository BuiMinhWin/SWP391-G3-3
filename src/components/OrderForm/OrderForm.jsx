import React from "react";
import { Box, Grid, InputAdornment, Paper, Typography } from "@mui/material";
import { Formik, Form } from "formik";
import * as Yup from "yup";
import TextFieldWrapper from "../FromUI/Textfield";
import SelectWrapper from "../FromUI/Select";
import countries from "../../data/countries.json";
import ButtonWrapper from "../FromUI/Button";
import koi_type from "../../data/koiTypes.json";
import koi_name from "../../data/koiVarieties.json";
import { createOrder } from "../../services/CustomerService";
import SideBar from "../SideBar/SideBar";
import HeaderBar from "../Header/Header/Nguyen";

// Initial Form State
const INITIAL_FORM_STATE = {
  origin: "",
  cityS: "",
  cityR: "",
  destination: "",
  receiverName: "",
  senderName: "",
  receiverAddress: "",
  senderAddress: "",
  receiverPhone: "",
  senderPhone: "",
  postalCodeS: "", // Use string for postal codes
  postalCodeR: "", // Use string for postal codes
  receiverNote: "",
  senderNote: "",
  description: "",
  discount: 0,
  koi_image: null,
  koi_name: "",
  koi_type: "",
  quantity: 1, // Set initial quantity to 1
  weight: 0.1, // Set initial weight to 0.1
  freight: false,
};

// Validation Schema with Yup
const FORM_VALIDATION = Yup.object().shape({
  origin: Yup.string().required("Vui lòng nhập địa điểm xuất phát"),
  destination: Yup.string().required("Vui lòng nhập địa điểm đến"),
  receiverName: Yup.string().required("Vui lòng nhập tên người nhận"),
  senderName: Yup.string().required("Vui lòng nhập tên người gửi"),
  receiverPhone: Yup.string()
    .matches(/^[0-9]+$/, "Số điện thoại phải là số")
    .required("Vui lòng nhập số điện thoại người nhận"),
  senderPhone: Yup.string()
    .matches(/^[0-9]+$/, "Số điện thoại phải là số")
    .required("Vui lòng nhập số điện thoại người gửi"),
  postalCodeS: Yup.string()
    .matches(/^[0-9]{5,6}$/, "Mã bưu điện phải là 5 hoặc 6 chữ số")
    .required("Vui lòng nhập mã bưu điện"),
  postalCodeR: Yup.string()
    .matches(/^[0-9]{5,6}$/, "Mã bưu điện phải là 5 hoặc 6 chữ số")
    .required("Vui lòng nhập mã bưu điện"),
  receiverNote: Yup.string().nullable(),
  senderNote: Yup.string().nullable(),
  description: Yup.string().nullable(), // Optional field for additional notes
  discount: Yup.number()
    .min(0, "Giảm giá không được nhỏ hơn 0")
    .required("Vui lòng nhập giảm giá"),
  cityS: Yup.string().required("Vui lòng chọn thành phố"),
  cityR: Yup.string().required("Vui lòng chọn thành phố"),
  koi_name: Yup.string().required("Vui lòng nhập tên cá Koi"),
  koi_type: Yup.string().required("Vui lòng nhập loại cá Koi"),
  quantity: Yup.number()
    .min(1, "Số lượng phải lớn hơn 0")
    .required("Vui lòng nhập số lượng"),
  weight: Yup.number()
    .min(0.1, "Cân nặng phải lớn hơn 0")
    .required("Vui lòng nhập cân nặng"),
  freight: Yup.boolean(),
});

const OrderForm = () => {
  return (
    <Formik
      initialValues={INITIAL_FORM_STATE}
      validationSchema={FORM_VALIDATION}
      onSubmit={async (values, { setSubmitting, setErrors }) => {
        console.log("Form values before submission:", values); // Debugging log

        const accountId = localStorage.getItem('accountId');
        console.log("Account ID:", accountId);

        try {
          const orderData = {
            accountId,
            origin: values.origin,
            cityS: values.cityS,
            cityR: values.cityR,
            destination: values.destination,
            receiverName: values.receiverName,
            senderName: values.senderName,
            receiverPhone: values.receiverPhone,
            senderPhone: values.senderPhone,
            postalCodeS: values.postalCodeS,
            postalCodeR: values.postalCodeR,
            receiverNote: values.receiverNote,
            senderNote: values.senderNote,
            orderNote: values.description, // Corresponds to description in form state
            discount: Number(values.discount),
            koi_image: values.koi_image,
            koi_name: values.koi_name,
            koi_type: values.koi_type,
            quantity: Number(values.quantity),
            weight: parseFloat(values.weight),
          };

          console.log("Order data ready for submission:", orderData); // Debugging log

          const response = await createOrder(orderData);
          console.log("Order created successfully:", response.data); // Success log
        } catch (error) {
          console.error("Error creating order:", error);
          setErrors({ submit: error.message });
        } finally {
          setSubmitting(false);
        }
      }}
      validateOnMount={true} // Ensure validation is checked on mount
    >
      {({ handleSubmit, isValid, errors }) => {
        console.log("Validation errors:", errors); // Log validation errors

        return (
          <Form onSubmit={handleSubmit}>
            <Box
              sx={{ display: "flex", flexDirection: "column", height: "100vh" }} component={"body"}
            >
              <Box
                sx={{
                  height: "64px",
                  bgcolor: "primary.main",
                  display: "flex",
                  alignItems: "center",
                  p: 2,
                }}
              >
                <HeaderBar/>
              </Box>

              {/* Sidebar + Content Container */}
              <Box sx={{ display: "flex", flex: 1 }}>
                <Box>
                  <SideBar />
                </Box>

                {/* Content */}
                <Box sx={{ flex: 1, p: 3, bgcolor: "#eeeeee" }}>
                  {/* Paper 1: Receiver Information */}
                  <Paper elevation={4} sx={{ padding: "20px" }}>
                    <Typography variant="h6" gutterBottom>
                      Địa chỉ lấy hàng *
                    </Typography>
                    <Grid container spacing={3}>
                      <Grid item xs={6}>
                        <TextFieldWrapper
                          name="senderName"
                          label="Tên người gửi"
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <TextFieldWrapper
                          name="senderPhone"
                          label="Điện thoại"
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <SelectWrapper
                          name="cityS"
                          label="Thành phố"
                          options={countries}
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <TextFieldWrapper
                          name="postalCodeS"
                          label="Postal Code"
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <TextFieldWrapper
                          name="origin"
                          label="Địa chỉ người gửi"
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <TextFieldWrapper
                          name="senderNote"
                          label="Hướng dẫn giao hàng"
                        />
                      </Grid>
                    </Grid>
                  </Paper>

                  {/* Paper 2: Receiver Information */}
                  <Paper elevation={4} sx={{ padding: "20px" }}>
                    <Typography variant="h6" gutterBottom>
                      Địa chỉ người nhận
                    </Typography>
                    <Grid container spacing={3}>
                      <Grid item xs={6}>
                        <TextFieldWrapper
                          name="receiverName"
                          label="Tên người nhận"
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <TextFieldWrapper
                          name="receiverPhone"
                          label="Điện thoại"
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <SelectWrapper
                          name="cityR"
                          label="Thành phố"
                          options={countries}
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <TextFieldWrapper
                          name="postalCodeR"
                          label="Postal Code"
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <TextFieldWrapper
                          name="destination"
                          label="Địa chỉ người nhận"
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <TextFieldWrapper
                          name="receiverNote"
                          label="Hướng dẫn giao hàng"
                        />
                      </Grid>
                    </Grid>
                  </Paper>

                  {/* Paper 3: Order Information */}
                  <Paper elevation={4} sx={{ padding: "20px" }}>
                    <Typography variant="h6" gutterBottom>
                      Thông tin bưu gửi
                    </Typography>
                    <Grid container spacing={3}>
                      <Grid item xs={6}>
                        <SelectWrapper
                          name="koi_type"
                          label="Koi Type"
                          options={koi_type}
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <SelectWrapper
                          name="koi_name"
                          label="Koi Variant"
                          options={koi_name}
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <TextFieldWrapper
                          name="weight"
                          label="Cân nặng trung bình"
                          InputProps={{
                            endAdornment: (
                              <InputAdornment position="end">kg</InputAdornment>
                            ),
                          }}
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <TextFieldWrapper name="quantity" label="Số lượng" />
                      </Grid>
                      <Grid item xs={6}>
                        <TextFieldWrapper name="description" label="Ghi chú" />
                      </Grid>
                      <Grid item xs={6}>
                        <TextFieldWrapper
                          name="discount"
                          label="Giảm giá"
                          InputProps={{
                            endAdornment: (
                              <InputAdornment position="end">%</InputAdornment>
                            ),
                          }}
                        />
                      </Grid>
                    </Grid>
                  </Paper>

                  <ButtonWrapper disabled={!isValid}>
                    Submit Order
                  </ButtonWrapper>
                </Box>
              </Box>
            </Box>
          </Form>
        );
      }}
    </Formik>
  );
};

export default OrderForm;
