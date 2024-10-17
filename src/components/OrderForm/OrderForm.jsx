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
import { createOrder, order, uploadDocument } from "../../services/CustomerService";
import { createOrderDetail } from "../../services/CustomerService";
// import SideBar from "../SideBar/SideBar";
// import HeaderBar from "../Header/Header/Nguyen";
import RadioGroupWrapper from "../FromUI/RadioGroup";
import CustomRadioGroup from "../FromUI/CustomRadioGroup";
import AccessibleIcon from "@mui/icons-material/Accessible";
import AccessibleForwardIcon from "@mui/icons-material/AccessibleForward";
import FileUpload from "../FromUI/FileUpload";
import CheckboxWrapper from "../FromUI/Checkbox";
import { useNavigate } from "react-router-dom";
import 'mapbox-gl/dist/mapbox-gl.css';
import axios from "axios";


// Initial Form State
const INITIAL_FORM_STATE = {
  origin: "",
  cityS: "",
  cityR: "",
  destination: "",
  distance: "",
  receiverName: "",
  senderName: "",
  receiverAddress: "",
  senderAddress: "",
  receiverPhone: "",
  senderPhone: "",
  postalCodeS: "",
  postalCodeR: "",
  receiverNote: "",
  senderNote: "",
  orderNote: "",
  document_file: null,
  discount: "",
  koi_image: "",
  koi_name: "",
  koi_type: "",
  quantity: 0,
  weight: 0.0,
  freight: "",
  additional_service: "",
  
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
  orderNote: Yup.string().nullable(), // Optional field for additional notes
  discount: Yup.string().nullable(),
  cityS: Yup.string().required("Vui lòng chọn thành phố"), //
  cityR: Yup.string().required("Vui lòng chọn thành phố"), //
  koi_name: Yup.string().required("Vui lòng nhập tên cá Koi"), //
  koi_type: Yup.string().required("Vui lòng nhập loại cá Koi"), //
  quantity: Yup.number()
    .min(1, "Số lượng phải lớn hơn 0")
    .required("Vui lòng nhập số lượng"),
  weight: Yup.number()
    .min(0.1, "Cân nặng phải lớn hơn 0")
    .required("Vui lòng nhập cân nặng"),
  freight: Yup.string().required("Vui lòng chọn phương thức vận chuyển"),
  additional_service: Yup.string().nullable(),
  termsOfService: Yup.boolean()
    .oneOf([true], "The terms and conditions must be accepted.")
    .required("The terms and conditions must be accepted."),
  document_file: Yup.mixed()
    .required("A file is required")
    .test(
      "fileSize",
      "File size must be less than 8MB",
      (value) => value && value.size <= 8 * 1024 * 1024
    )
    .test(
      "fileFormat",
      "Only PDF file are allowed",
      (value) => value && value.type === "application/pdf"
    ),
});



const OrderForm = () => {
  const navigate = useNavigate();  
  const calculateDistance = (lat1, lon1, lat2, lon2) => {
    const R = 6371;
    const dLat = (lat2 - lat1) * (Math.PI / 180);
    const dLon = (lon2 - lon1) * (Math.PI / 180);
    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(lat1 * (Math.PI / 180)) * Math.cos(lat2 * (Math.PI / 180)) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
  };

  return (
    <Formik
      initialValues={INITIAL_FORM_STATE}
      validationSchema={FORM_VALIDATION}
      onSubmit={async (values, { setSubmitting, setErrors }) => {
        try {
          const accountId = localStorage.getItem("accountId");
          const senderAddress = `${values.origin}`;
          const receiverAddress = `${values.destination}`;

          const senderCoordinatesResponse = await axios.get(`https://api.mapbox.com/geocoding/v5/mapbox.places/${encodeURIComponent(senderAddress)}.json?access_token=pk.eyJ1IjoiYm10aGFuZzIwMDQiLCJhIjoiY20xdWJsYTV2MGF4bDJybXlmdHVnZ2k3cyJ9.08eXbVTzmWjVZp4NhGnoVw`);
          const senderCoordinates = senderCoordinatesResponse.data.features[0].center;

          const receiverCoordinatesResponse = await axios.get(`https://api.mapbox.com/geocoding/v5/mapbox.places/${encodeURIComponent(receiverAddress)}.json?access_token=pk.eyJ1IjoiYm10aGFuZzIwMDQiLCJhIjoiY20xdWJsYTV2MGF4bDJybXlmdHVnZ2k3cyJ9.08eXbVTzmWjVZp4NhGnoVw`);
          const receiverCoordinates = receiverCoordinatesResponse.data.features[0].center;

          const orderData = {
            ...values,
            accountId,
            origin: `${values.origin}, ${values.cityS}, ${values.postalCodeS}`,
            destination: `${values.destination}, ${values.cityR}, ${values.postalCodeR}`,
            freight: values.freight,
            receiverName: values.receiverName,
            senderName: values.senderName,
            receiverPhone: values.receiverPhone,
            senderPhone: values.senderPhone,
            receiverNote: values.receiverNote,
            senderNote: values.senderNote,
            orderNote: values.orderNote,
            distance : calculateDistance(senderCoordinates[1], senderCoordinates[0], receiverCoordinates[1], receiverCoordinates[0]),
          };
          
          const orderResponse = await createOrder(orderData);
          if (!orderResponse?.orderId) {
            throw new Error("Order ID not found in the response");
          }

          const newOrderId = orderResponse.orderId;
          console.log("Order created with ID:", newOrderId);

          const uploadResponse = await uploadDocument(values.document_file, newOrderId);
          console.log('File uploaded successfully:', uploadResponse);

          const orderDetails = await order(newOrderId);
          console.log("Order Details:", orderDetails);

          const orderDetailData = {
            orderId: newOrderId,
            quantity: Number(values.quantity),
            weight: parseFloat(values.weight),
            discount: values.discount,
            koiName: values.koi_name,
            koiType: values.koi_type,
          };
          const orderDetailResponse = await createOrderDetail(orderDetailData);
          console.log(
            "Order detail created successfully:",
            orderDetailResponse
          );

          console.log("Order created successfully with ID:", newOrderId);
          navigate("/checkout", { state: { orderId: newOrderId } });
        } catch (error) {
          alert("Upload failed, please try again");
          console.error("Error creating order:", error);
          setErrors({ submit: error.message });
        } finally {
          setSubmitting(false);
        }
      }}
      validateOnMount={true}
    >
      {({ handleSubmit, errors }) => {
        console.log("Validation errors:", errors); // Log validation errors

        return (
          <Form onSubmit={handleSubmit}>
                {/* Content */}
                <>
                <Box sx={{ p: 4, bgcolor: "#eeeeee" }}>
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
                          multiline
                          rows={3}
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
                          multiline
                          rows={3}
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
                      <Grid item xs={3}>
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
                      <Grid item xs={3}>
                        <TextFieldWrapper
                          name="quantity"
                          label="Số lượng"
                          type="number"
                          slotProps={{
                            inputLabel: {
                              shrink: true,
                            },
                          }}
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <FileUpload name="document_file" />
                      </Grid>
                      <Grid item xs={12}>
                        <TextFieldWrapper
                          name="description"
                          label="Ghi chú"
                          multiline
                          rows={4}
                        />
                      </Grid>

                      <Grid item xs={12}>
                        <CustomRadioGroup
                          name="freight"
                          options={[
                            {
                              value: "Dịch vụ tiêu chuẩn",
                              label: "Dịch vụ tiêu chuẩn",
                              description:
                                "Giao theo tiêu chuẩn dịch vụ 1-4 ngày",
                              icon: <AccessibleIcon fontSize="large" />,
                              helpText:
                                "Phí vận chuyển ước tính sẽ bao gồm phụ phí và trừ đi các khoản chiến khấu/giảm giá bởi khuyến mãi.",
                            },
                            {
                              value: "Dịch vụ hỏa tốc",
                              label: "Dịch vụ hỏa tốc",
                              description:
                                "Giao theo tiêu chuẩn dịch vụ 1-3 giờ",
                              icon: <AccessibleForwardIcon fontSize="large" />,
                              helpText:
                                "Phí vận chuyển ước tính sẽ bao gồm phụ phí và trừ đi các khoản chiến khấu/giảm giá bởi khuyến mãi.",
                            },
                          ]}
                        />
                      </Grid>
                      <Grid item xs={3}>
                        <TextFieldWrapper
                          name="discount"
                          label="Mã giảm giá"
                          InputProps={{
                            endAdornment: (
                              <InputAdornment position="end">%</InputAdornment>
                            ),
                          }}
                        />
                      </Grid>
                      <Grid
                        item
                        xs={3}
                        justifyContent="center" // Center horizontally
                        alignItems="center"
                      >
                        <RadioGroupWrapper
                          name="additional_service"
                          legend="Bảo hiểm sự cố"
                          defaultValue="No"
                          options={[
                            {
                              value: "Yes",
                              label: "Có",
                            },
                            {
                              value: "No",
                              label: "Không",
                            },
                          ]}
                        />
                      </Grid>
                    </Grid>
                  </Paper>
                  <CheckboxWrapper
                    name="termsOfService"
                    legend="Terms Of Service"
                    label="I agree"
                  />
                  <ButtonWrapper>Submit Order</ButtonWrapper>
                </Box>
                </>
          </Form>
        );
      }}
    </Formik>
  );
};

export default OrderForm;
