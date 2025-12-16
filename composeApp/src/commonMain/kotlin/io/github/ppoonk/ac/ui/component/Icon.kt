package io.github.ppoonk.ac.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.brands.AmazonPay
import compose.icons.fontawesomeicons.solid.AngleDown
import compose.icons.fontawesomeicons.solid.AngleLeft
import compose.icons.fontawesomeicons.solid.AngleRight
import compose.icons.fontawesomeicons.solid.AngleUp
import compose.icons.fontawesomeicons.solid.Bars
import compose.icons.fontawesomeicons.solid.Bug
import compose.icons.fontawesomeicons.solid.CheckSquare
import compose.icons.fontawesomeicons.solid.ClipboardList
import compose.icons.fontawesomeicons.solid.Copy
import compose.icons.fontawesomeicons.solid.Edit
import compose.icons.fontawesomeicons.solid.EllipsisV
import compose.icons.fontawesomeicons.solid.Envelope
import compose.icons.fontawesomeicons.solid.Gift
import compose.icons.fontawesomeicons.solid.Home
import compose.icons.fontawesomeicons.solid.InfoCircle
import compose.icons.fontawesomeicons.solid.Lock
import compose.icons.fontawesomeicons.solid.Plus
import compose.icons.fontawesomeicons.solid.Search
import compose.icons.fontawesomeicons.solid.Server
import compose.icons.fontawesomeicons.solid.ShieldAlt
import compose.icons.fontawesomeicons.solid.ShoppingCart
import compose.icons.fontawesomeicons.solid.SignOutAlt
import compose.icons.fontawesomeicons.solid.Sitemap
import compose.icons.fontawesomeicons.solid.Sort
import compose.icons.fontawesomeicons.solid.SyncAlt
import compose.icons.fontawesomeicons.solid.TicketAlt
import compose.icons.fontawesomeicons.solid.TrashAlt
import compose.icons.fontawesomeicons.solid.User

@Composable
fun ACIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.size(24.dp),
        tint = tint
    )

}

@Composable
fun ACIconSmall(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.size(16.dp),
        tint = tint
    )
}

object ACIconDefault {
    val AngleLeft = FontAwesomeIcons.Solid.AngleLeft
    val AngleRight = FontAwesomeIcons.Solid.AngleRight
    val AngleUp = FontAwesomeIcons.Solid.AngleUp
    val AngleDown = FontAwesomeIcons.Solid.AngleDown

    val Search = FontAwesomeIcons.Solid.Search
    val DrawerOpen = FontAwesomeIcons.Solid.Bars
    val Plus = FontAwesomeIcons.Solid.Plus
    val Edit = FontAwesomeIcons.Solid.Edit
    val Trash = FontAwesomeIcons.Solid.TrashAlt
    val MoreV = FontAwesomeIcons.Solid.EllipsisV
    val Home = FontAwesomeIcons.Solid.Home
    val User = FontAwesomeIcons.Solid.User
    val Ticket = FontAwesomeIcons.Solid.TicketAlt
    val Sitemap = FontAwesomeIcons.Solid.Sitemap
    val Copy = FontAwesomeIcons.Solid.Copy
    val Check = FontAwesomeIcons.Solid.CheckSquare

    val Bug = FontAwesomeIcons.Solid.Bug
    val Shield = FontAwesomeIcons.Solid.ShieldAlt
    val SignOut = FontAwesomeIcons.Solid.SignOutAlt
    val Lock = FontAwesomeIcons.Solid.Lock
    val Sort = FontAwesomeIcons.Solid.Sort
    val Sync = FontAwesomeIcons.Solid.SyncAlt
    val Cart = FontAwesomeIcons.Solid.ShoppingCart
    val Gift = FontAwesomeIcons.Solid.Gift
    val ClipboardList = FontAwesomeIcons.Solid.ClipboardList
    val Server = FontAwesomeIcons.Solid.Server
    val Email = FontAwesomeIcons.Solid.Envelope
    val Pay = FontAwesomeIcons.Brands.AmazonPay
    val Info = FontAwesomeIcons.Solid.InfoCircle
}
