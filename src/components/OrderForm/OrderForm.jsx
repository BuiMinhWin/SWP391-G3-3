import { Box, Grid, Paper, Typography} from "@mui/material";
import { Formik, Form } from "formik";
import * as Yup from "yup";
import SideBar from "../SideBar/SideBar";
import TextField from "../FromUI/Textfield";
import Select from "../FromUI/Select/index";
import contries from "../../data/countries.json";
import Checkbox from "../FromUI/Checkbox"
import Button from "../FromUI/Button"

const INITIAL_FORM_STATE = {
  fullName: "",
  phone: "",
  postalCode: "",
  province: "",
  pickupAdress: "",
  pickupAdressInstruction: "",
  receiverFullName: "",
  receiverPhone: "",
  receiverPostalCode: "",
  receiverProvince: "",
  receiverPickupAdress: "",
  receiverpickupAdressInstruction: "",
  feight: false
};

const FORM_VALIDATION = Yup.object().shape({
  fullName: Yup.string().required("Vui lòng không để trống"),
  phone: Yup.string().required("Vui lòng không để trống"),
  postalCode: Yup.string().required("Vui lòng không để trống"),
  freight: Yup.boolean().oneOf([true], "Must be selected").required("Must select"),
});

const OrderForm = () => {
  return (
    <Formik
      initialValues={{ ...INITIAL_FORM_STATE }}
      validationSchema={FORM_VALIDATION}
      onSubmit={(values) => {
        console.log(values);
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
                        options={contries}
                      />
                    </Grid>
                    <Grid item xs={6}>
                      <Checkbox name="freight" legend = "InVN" label="feight" />
                    </Grid>
                    <Grid item xs={6}>
                      <TextField name="pickupAdress" label="Pickup Address" />
                    </Grid>
                    <Grid item xs={6}>
                      <TextField
                        name="pickupAdressInstruction"
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
                        name="receiverPickupAdress"
                        label="Receiver Pickup Address"
                      />
                    </Grid>
                    <Grid item xs={6}>
                      <TextField
                        name="receiverpickupAdressInstruction"
                        label="Receiver Pickup Address Instruction"
                      />
                    </Grid>
                  </Grid>
                </Paper>

                {/* Submit Button */}
                <Box mt={3}>
                    <Button>Submit Form</Button>
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
