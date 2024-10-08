import { Box, Typography } from "@mui/material";
import SideBar from "../../components/SideBar/SideBar";


const UserPage = () => {
  return (
    <Box sx={{ display: "flex" }}>
      {/* SideBar */}

      <Box>
        <SideBar />
      </Box>

      <Box display={"flex"} flexDirection={"column"}>
        {/* Header */}
        <Box>Hello </Box>

        {/* Content */}
        <Box>
          <Typography>
            Lorem ipsum dolor sit amet consectetur adipisicing elit. Minus at
            maxime excepturi magnam labore eos corrupti nam tenetur nemo, in
            cumque est obcaecati commodi accusantium ullam alias consectetur
            minima recusandae.
          </Typography>
        </Box>
      </Box>
    </Box>
  );
};
export default UserPage;
