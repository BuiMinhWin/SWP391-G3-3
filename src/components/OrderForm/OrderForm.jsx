import { Box, Grid2, Paper, TextField, Typography } from "@mui/material";
import { Formik, Form } from "formik";
import * as Yup from "yup";
import SideBar from "../SideBar/SideBar";
// import TextField from '../FromUI/Textfield'

const INITIAL_FROM_STATE = {
  fullName:'',
  phone:'',
  postalCode:'',
  province:'',
  pickupAdress:'',
  pickupAdressInstruction:'',

  receiverFullName:'',
  receiverPhone:'',
  receiverPostalCode:'',
  receiverProvince:'',
  receiverPickupAdress:'',
  receiverpickupAdressInstruction:'',

  typeOfFish:'',
  weight:'',
  quantity:'',
  sellerNote:'',
  feight:'',
  discount:'',
  totalAmount:''
};
const FORM_VALIDATION = Yup.object().shape({
  fullName:Yup.string().required('Vui lòng không để trống'),


});

const OrderForm = () => {
  return (
    <Formik
      initialValues={{ ...INITIAL_FROM_STATE }}
      validationSchema={FORM_VALIDATION}
      onSubmit={(values) => {
        console.log(values);
      }}
    >
      <Box
        sx={{ display: "flex", backgroundColor: "darkgray", height: "100%" }}
      >
        {/* SideBar */}

        <Box flex={1}>
          <SideBar />
        </Box>

        <Box display={"flex"} flex={11} flexDirection={"column"}>
          {/* Header */}
          <Box>Reservation for header </Box>

          {/* Content */}
          <Box display={"flex"} flexDirection={"column"}>
            <Box
              display={"flex"}
              justifyContent={"center"}
              flexDirection={"column"}
              gap={"60px"}
              padding={"0 130px"}
            >
              <Paper elevation={4} sx={{ padding: "5px" }}>
                <Form>
                  <Typography variant="h6">Địa chỉ người nhận</Typography>
                  <Grid2
                    container
                    rowSpacing={1}
                    columnSpacing={{ xs: 1, sm: 2, md: 3 }}
                  >
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                  </Grid2>
                </Form>
              </Paper>
              <Paper elevation={4} sx={{ padding: "5px" }}>
                <Form>
                  <Grid2
                    container
                    rowSpacing={1}
                    columnSpacing={{ xs: 1, sm: 2, md: 3 }}
                  >
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                  </Grid2>
                </Form>
              </Paper>
              <Paper elevation={4} sx={{ padding: "5px" }}>
                <Form>
                  <Grid2
                    container
                    rowSpacing={1}
                    columnSpacing={{ xs: 1, sm: 2, md: 3 }}
                  >
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                  </Grid2>
                </Form>
              </Paper>
              <Paper elevation={4} sx={{ padding: "5px" }}>
                <Form>
                  <Grid2
                    container
                    rowSpacing={1}
                    columnSpacing={{ xs: 1, sm: 2, md: 3 }}
                  >
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                  </Grid2>
                </Form>
              </Paper>
              <Paper elevation={4} sx={{ padding: "5px" }}>
                <Form>
                  <Grid2
                    container
                    rowSpacing={1}
                    columnSpacing={{ xs: 1, sm: 2, md: 3 }}
                  >
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                    <Grid2 size={6}>
                      <TextField
                        fullWidth
                        label="Standard"
                        variant="standard"
                      />
                    </Grid2>
                  </Grid2>
                </Form>
              </Paper>
            </Box>
            <Box>Hello</Box>
            <Box>Hello</Box>
            <Box>Hello</Box>
            <Box>Hello</Box>
          </Box>
        </Box>
      </Box>
    </Formik>
  );
};

export default OrderForm;
