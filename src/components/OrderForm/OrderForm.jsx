import React from "react";
import { Box, Grid, InputAdornment, Paper, Typography } from "@mui/material";
import { Formik, Form } from "formik";
import * as Yup from "yup";
import SideBar from "../SideBar/SideBar";
import TextField from "../FromUI/Textfield";
import Select from "../FromUI/Select/index";
import countries from "../../data/countries.json";
import Checkbox from "../FromUI/Checkbox";
import ButtonWrapper from "../FromUI/Button";
import koi_type from "../../data/koiTypes.json";
import koi_name from "../../data/koiVarieties.json";
// import FileInput from "../FromUI/File";

import { createOrder } from "../../services/CustomerService";

// const FILE_SIZE = 1024 * 1024 * 5; // 5MB
// const SUPPORTED_FORMATS = ["image/jpg", "image/jpeg", "image/gif", "image/png"];

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
  postalCodeS: 0,
  postalCodeR: 0,
  receiverNote: "",
  senderNote: "",
  description: "",
  discount: 0,
  koi_image: null,
  koi_name: "",
  koi_type: "",
  quantity: 0,
  weight: 0.0,
  freight: false,
  // files: null,
};

const FORM_VALIDATION = Yup.object().shape({
  origin: Yup.string().required("Vui lòng nhập địa điểm xuất phát"),
  destination: Yup.string().required("Vui lòng nhập địa điểm đến"),
  receiverName: Yup.string().required("Vui lòng nhập tên người nhận"),
  senderName: Yup.string().required("Vui lòng nhập tên người gửi"),
  receiverAddress: Yup.string().required("Vui lòng nhập địa chỉ người nhận"),
  senderAddress: Yup.string().required("Vui lòng nhập địa chỉ người gửi"),
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
  description: Yup.string().nullable(),
  discount: Yup.number()
    .min(0, "Giảm giá không được nhỏ hơn 0")
    .required("Vui lòng nhập giảm giá"),
  // koi_image: Yup.mixed()
  //   .nullable()
  //   .required("Vui lòng tải lên hình ảnh cá Koi"),
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
        console.log("Form values before submission:", values); // First check

        try {
          // Log right before preparing the data
          console.log("Preparing orderData:", values);

          const orderData = {
            origin: values.origin,
            cityS: `${values.cityS} - ${values.origin}`,
            cityR: `${values.cityR} - ${values.destination}`,
            destination: values.destination,
            receiverName: values.receiverName,
            senderName: values.senderName,
            senderAddress: values.senderAddress,
            receiverPhone: values.receiverPhone,
            senderPhone: values.senderPhone,
            postalCodeS: values.postalCodeS,
            postalCodeR: values.postalCodeR,
            receiverNote: values.receiverNote,
            senderNote: values.senderNote,
            orderNote: values.description,
            discount: Number(values.discount),
            koi_image: values.koi_image,
            koi_name: values.koi_name,
            koi_type: values.koi_type,
            quantity: Number(values.quantity),
            weight: parseFloat(values.weight),
          };

          console.log("Order data ready for submission:", orderData); // Second check

          const response = await createOrder(orderData);
          console.log("Order created successfully:", response.data); // Third check
        } catch (error) {
          console.error("Error creating order:", error);
          setErrors({ submit: error.message });
        } finally {
          setSubmitting(false);
        }
      }}
    >
      {({ handleSubmit }) => (
        <Form onSubmit={handleSubmit}>
          <Box
            sx={{ display: "flex", flexDirection: "column", height: "100vh" }}
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
              <h1 style={{ color: "white" }}>Top Nav Bar</h1>
            </Box>

            {/* Sidebar + Content Container */}
            <Box sx={{ display: "flex", flex: 1 }}>
              <Box sx={{ width: "250px", bgcolor: "secondary.main", p: 2 }}>
                <h2 style={{ color: "white" }}>Sidebar</h2>
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
                      <TextField name="senderName" label="Tên người gửi" />
                    </Grid>
                    <Grid item xs={6}>
                      <TextField name="senderPhone" label="Điện thoại" />
                    </Grid>
                    <Grid item xs={6}>
                      <Select
                        name="cityS"
                        label="Thành phố"
                        options={countries}
                      />
                    </Grid>
                    <Grid item xs={6}>
                      <TextField name="postalCodeS" label="Postal Code" />
                      {/* <Checkbox name="freight" legend="InVN" label="Freight" /> */}
                    </Grid>
                    <Grid item xs={6}>
                      <TextField name="origin" label="Địa chỉ người gửi" />
                    </Grid>
                    <Grid item xs={6}>
                      <TextField
                        name="senderNote"
                        label="Hướng dẫn giao hàng"
                      />
                    </Grid>
                  </Grid>
                </Paper>

                {/* Paper 2: Sender Information */}
                <Paper elevation={4} sx={{ padding: "20px" }}>
                  <Typography variant="h6" gutterBottom>
                    Địa chỉ người nhận
                  </Typography>
                  <Grid container spacing={3}>
                    <Grid item xs={6}>
                      <TextField name="receiverName" label="Tên người nhận" />
                    </Grid>
                    <Grid item xs={6}>
                      <TextField name="receiverPhone" label="Điện thoại" />
                    </Grid>

                    <Grid item xs={6}>
                      <Select
                        name="cityR"
                        label="Thành phố"
                        options={countries}
                      />
                    </Grid>

                    <Grid item xs={6}>
                      <TextField name="postalCodeR" label="Postal Code" />
                    </Grid>
                    <Grid item xs={6}>
                      <TextField
                        name="destination"
                        label="Địa chỉ người nhận"
                      />
                    </Grid>

                    <Grid item xs={6}>
                      <TextField
                        name="receiverNote"
                        label="Hướng dẫn giao hàng"
                      ></TextField>
                    </Grid>
                  </Grid>
                </Paper>
                <Paper elevation={4} sx={{ padding: "20px" }}>
                  <Typography variant="h6" gutterBottom>
                    Thông tin bưu gửi
                  </Typography>
                  <Grid container spacing={3}>
                    <Grid item xs={6}>
                      <Select
                        name="koi_type"
                        label="Koi Type"
                        options={koi_type}
                      />
                    </Grid>
                    <Grid item xs={6}>
                      <Select
                        name="koi_name"
                        label="Koi Variant"
                        options={koi_name}
                      />
                    </Grid>

                    <Grid item xs={6}>
                      <TextField
                        name="weight"
                        label="Cân nặng trung bình"
                        slotProps={{
                          input: {
                            startAdornment: (
                              <InputAdornment
                                position="start"
                                sx={{
                                  display: "flex",
                                  flex: "center",
                                }}
                              >
                                kg
                              </InputAdornment>
                            ),
                          },
                        }}
                      />
                    </Grid>

                    <Grid item xs={6}>
                      <TextField
                        name="quantity"
                        label="Quantities"
                        type="number"
                        slotProps={{
                          inputLabel: {
                            shrink: true,
                          },
                        }}
                      />
                    </Grid>

                    <Grid item xs={12}>
                      <TextField
                        name="orderNote"
                        label="Lưu ý của người gửi: "
                        multiline
                        rows={4}
                      ></TextField>
                    </Grid>
                  </Grid>
                </Paper>
                {/* Submit Button */}
                <Box mt={3}>
                  <ButtonWrapper>Submit Order</ButtonWrapper>
                </Box>
              </Box>
            </Box>
          </Box>
        </Form>
      )}
    </Formik>
  );
};

export default OrderForm;
