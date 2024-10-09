import React from "react";
import { Box, Grid, Paper, Typography } from "@mui/material";
import { Formik, Form } from "formik";
import * as Yup from "yup";
import SideBar from "../SideBar/SideBar";
import TextField from "../FromUI/Textfield";
import Select from "../FromUI/Select/index";
import countries from "../../data/countries.json"; 
import Checkbox from "../FromUI/Checkbox";
import ButtonWrapper from "../FromUI/Button"; // Updated Button import
import { createOrder } from "../../services/CustomerService";

const INITIAL_FORM_STATE = {
  fullName: "",
  phone: "",
  postalCode: "",
  province: "",
  pickupAddress: "",
  pickupAddressInstruction: "",
  receiverFullName: "",
  receiverPhone: "",
  receiverPostalCode: "",
  receiverProvince: "",
  receiverPickupAddress: "",
  receiverPickupAddressInstruction: "",
  freight: false,
};

const FORM_VALIDATION = Yup.object().shape({
  fullName: Yup.string().required("Vui lòng không để trống"),
  phone: Yup.string().required("Vui lòng không để trống"),
  postalCode: Yup.string().required("Vui lòng không để trống"),
  freight: Yup.boolean()
    .oneOf([true], "Must be selected")
    .required("Must select"),
});

const OrderForm = () => {
  return (
    <Formik
      initialValues={INITIAL_FORM_STATE}
      validationSchema={FORM_VALIDATION}
      onSubmit={async (values, { setSubmitting, setErrors }) => {
        try {
          // Define the structure for the new order
          const orderData = {
            customer: {
              fullName: values.fullName,
              phone: values.phone,
              postalCode: values.postalCode,
              province: values.province,
              pickupAddress: values.pickupAddress,
              pickupAddressInstruction: values.pickupAddressInstruction,
            },
            receiver: {
              fullName: values.receiverFullName,
              phone: values.receiverPhone,
              postalCode: values.receiverPostalCode,
              province: values.receiverProvince,
              pickupAddress: values.receiverPickupAddress,
              pickupAddressInstruction: values.receiverPickupAddressInstruction,
            },
            freight: values.freight,
          };

          // Call the createOrder API function
          const response = await createOrder(orderData);
          console.log("Order created successfully:", response.data);
          // Optionally reset the form or show a success message
        } catch (error) {
          console.error("Error creating order:", error);
          // Handle errors, e.g., setErrors({ submit: error.message });
        } finally {
          setSubmitting(false);
        }
      }}
    >
      {({ handleSubmit }) => (
        <Form onSubmit={handleSubmit}>
          <Box
            sx={{
              display: "flex",
              backgroundColor: "darkgray",
              height: "100%",
            }}
          >
            {/* SideBar */}
            <Box flex={1}>
              <SideBar />
            </Box>

            <Box display={"flex"} flex={11} flexDirection={"column"}>
              {/* Header */}
              <Box>Reservation for header</Box>

              {/* Content */}
              <Box
                display={"flex"}
                flexDirection={"column"}
                gap={3}
                padding={"0 130px"}
              >
                {/* Paper 1: Receiver Information */}
                <Paper elevation={4} sx={{ padding: "20px" }}>
                  <Typography variant="h6" gutterBottom>
                    Địa chỉ người nhận
                  </Typography>
                  <Grid container spacing={3}>
                    <Grid item xs={6}>
                      <TextField name="fullName" label="Full Name" />
                    </Grid>
                    <Grid item xs={6}>
                      <TextField name="phone" label="Phone" />
                    </Grid>
                    <Grid item xs={6}>
                      <Select
                        name="postalCode"
                        label="Postal Code"
                        options={countries}
                      />
                    </Grid>
                    <Grid item xs={6}>
                      <Checkbox name="freight" legend="InVN" label="Freight" />
                    </Grid>
                    <Grid item xs={6}>
                      <TextField name="pickupAddress" label="Pickup Address" />
                    </Grid>
                    <Grid item xs={6}>
                      <TextField
                        name="pickupAddressInstruction"
                        label="Pickup Address Instruction"
                      />
                    </Grid>
                  </Grid>
                </Paper>

                {/* Paper 2: Sender Information */}
                <Paper elevation={4} sx={{ padding: "20px" }}>
                  <Typography variant="h6" gutterBottom>
                    Thông tin người bán
                  </Typography>
                  <Grid container spacing={3}>
                    <Grid item xs={6}>
                      <TextField
                        name="receiverFullName"
                        label="Receiver Full Name"
                      />
                    </Grid>
                    <Grid item xs={6}>
                      <TextField name="receiverPhone" label="Receiver Phone" />
                    </Grid>
                    <Grid item xs={6}>
                      <TextField
                        name="receiverPostalCode"
                        label="Receiver Postal Code"
                      />
                    </Grid>
                    <Grid item xs={6}>
                      <TextField
                        name="receiverProvince"
                        label="Receiver Province"
                      />
                    </Grid>
                    <Grid item xs={6}>
                      <TextField
                        name="receiverPickupAddress"
                        label="Receiver Pickup Address"
                      />
                    </Grid>
                    <Grid item xs={6}>
                      <TextField
                        name="receiverPickupAddressInstruction"
                        label="Receiver Pickup Address Instruction"
                      />
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
