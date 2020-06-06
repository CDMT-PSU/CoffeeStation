package cdmtpsu.coffee.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class CenterLayout implements LayoutManager {
    public CenterLayout() {
    }

    @Override
    public void addLayoutComponent(String name, Component component) {
    }

    @Override
    public void removeLayoutComponent(Component component) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        var width = 0;
        var height = 0;
        for (var component : parent.getComponents()) {
            var size = component.getPreferredSize();
            if (width < size.width) {
                width = size.width;
            }
            height += component.getPreferredSize().height;
        }
        var insets = parent.getInsets();
        return new Dimension(width + insets.left + insets.right,
                height + insets.top + insets.bottom);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        var sumHeight = 0;
        for (var component : parent.getComponents()) {
            sumHeight += component.getPreferredSize().height;
        }
        var offset = 0;
        for (var component : parent.getComponents()) {
            var width = component.getPreferredSize().width;
            var height = component.getPreferredSize().height;
            var x = (parent.getWidth() - width) / 2;
            var y = (parent.getHeight() - sumHeight) / 2 + offset;
            component.setBounds(x, y, width, height);
            offset += height;
        }
    }
}
