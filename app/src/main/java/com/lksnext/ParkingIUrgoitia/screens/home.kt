package com.lksnext.ParkingIUrgoitia.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onMakeReservationClick: () -> Unit = {},
    onViewReservationsClick: () -> Unit = {},
    onEditReservationClick: () -> Unit = {},
    onDeleteReservationClick: () -> Unit = {},
    onAddVehicleClick: () -> Unit = {}
) {

    val lksOrange = Color(0xFFF25C05)

    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 3.dp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = lksOrange,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Logo",
                            tint = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "LKS next Parking",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = lksOrange
                        )
                    }

                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = lksOrange
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFFFAFAFA)
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Hello John!",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Manage your parking reservations with ease",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            HomeActionButton(
                text = "Make a Reservation",
                icon = Icons.Default.Assignment,
                lksOrange = lksOrange,
                onClick = { onMakeReservationClick() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            HomeActionButton(
                text = "View Reservations",
                icon = Icons.Default.Visibility,
                lksOrange = lksOrange,
                onClick = { onViewReservationsClick() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            HomeActionButton(
                text = "Edit Reservation",
                icon = Icons.Default.Edit,
                lksOrange = lksOrange,
                onClick = { onEditReservationClick() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            HomeActionButton(
                text = "Delete Reservation",
                icon = Icons.Default.Delete,
                lksOrange = lksOrange,
                onClick = {  onDeleteReservationClick() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            HomeActionButton(
                text = "Add Vehicle",
                icon = Icons.Outlined.AddCircleOutline,
                lksOrange = lksOrange,
                onClick = { onAddVehicleClick() }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun HomeActionButton(
    text: String,
    icon: ImageVector,
    lksOrange: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val borderColor = if (isHovered) lksOrange else Color(0xFFE5E7EB)
    val shadowElevation = if (isHovered) 8.dp else 0.dp
    val circleBackgroundColor = if (isHovered) lksOrange else lksOrange.copy(alpha = 0.1f)
    val iconColor = if (isHovered) Color.White else lksOrange

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .hoverable(interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current
            ) { onClick() },
        shape = RoundedCornerShape(36.dp),
        border = BorderStroke(1.dp, borderColor),
        color = Color.White,
        shadowElevation = shadowElevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.Black
            )

            Surface(
                shape = CircleShape,
                color = circleBackgroundColor,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = icon,
                        contentDescription = text,
                        tint = iconColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}