// DeliveryStatusPopup.js
import React, { useEffect, useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  IconButton,
  CircularProgress,
  Typography,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import {
  Timeline,
  TimelineItem,
  TimelineSeparator,
  TimelineConnector,
  TimelineContent,
  TimelineDot,
} from "@mui/lab";

import { getDeliveryStatusByOrderId } from "../../services/CustomerService";

const DeliveryStatusPopup = ({ open, onClose, orderId }) => {
  const [deliveryStatus, setDeliveryStatus] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDeliveryStatus = async () => {
      try {
        const data = await getDeliveryStatusByOrderId(orderId);
        setDeliveryStatus(data);
      } catch (error) {
        console.error("Failed to fetch delivery status:", error);
      } finally {
        setLoading(false);
      }
    };

    if (orderId) fetchDeliveryStatus();
  }, [orderId]);

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      <DialogTitle>
        Delivery Status
      </DialogTitle>
      <DialogContent dividers>
        {loading ? (
          <CircularProgress />
        ) : deliveryStatus.length === 0 ? (
          <Typography>No delivery status available.</Typography>
        ) : (
          <Timeline position="alternate">
            {deliveryStatus.map((status, index) => (
              <TimelineItem key={index}>
                <TimelineSeparator>
                  <TimelineDot color="primary" />
                  {index < deliveryStatus.length - 1 && <TimelineConnector />}
                </TimelineSeparator>
                <TimelineContent>
                  <Typography variant="h6">
                    {new Date(status.timeTracking).toLocaleString()}
                  </Typography>
                  <Typography>{status.currentLocate}</Typography>
                </TimelineContent>
              </TimelineItem>
            ))}
          </Timeline>
        )}
      </DialogContent>
    </Dialog>
  );
};

export default DeliveryStatusPopup;
