package GUI.GuildPanel;

import CraftingResources.CraftingResource;
import GUI.GUIElements;
import MarketSimulator.MarketResource;
import MerchantResources.Resource;

import java.awt.*;
import java.math.BigDecimal;

/**
 * Created by Sam on 13/07/2016.
 */
public class ResourceButton extends GUIElements.MyButton {
    private GuildPanel guildPanel;
    private ResourceButton thisButton;
    private Resource resource;

    public ResourceButton(Resource resource, boolean enabled, boolean darker, GuildPanel guildPanel) {
        super("", enabled, new Color(30, 30, 30), new Color(25, 25, 25), true);
        this.resource = resource;
        this.guildPanel = guildPanel;
        if (!enabled) {
            setForeground(Color.white);
        }
        thisButton = this;
        this.addActionListener(e -> guildPanel.setSelectedResource(thisButton));
    }

    public String getResourceName() {
        return resource.name();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int yOffset;
        if (!isEnabled()) {
            g.setFont(thisButton.getFont().deriveFont(14f));
            g.setColor(new Color(255, 140, 0));
            yOffset = 7;
        } else {
            g.setFont(thisButton.getFont().deriveFont(12f));
            g.setColor(Color.white);
            yOffset = 6;
        }
        g.drawString(resource.name(), 10, getHeight() / 2 + yOffset);
        g.setFont(thisButton.getFont().deriveFont(10f));
        g.setColor(new Color(200, 200, 200));
        String str;
        if (guildPanel.getPanelType() == GuildPanel.PanelType.MERCHANT) {
            BigDecimal price = new BigDecimal(((MarketResource) resource).getAveragePrice());
            BigDecimal roundedPrice = price.setScale(2, BigDecimal.ROUND_HALF_UP);
            str = "C" + roundedPrice + " (D" + ((MarketResource) resource).getDemand() + "/ S"
                    + ((MarketResource) resource).getSupply() + ")";
        } else {
            str = "C" + ((CraftingResource) resource).value();
        }
        int stringLength = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
        g.drawString(str, getWidth() - (stringLength + 10), getHeight() / 2 + 5);
    }
}
