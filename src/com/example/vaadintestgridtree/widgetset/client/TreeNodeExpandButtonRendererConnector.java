package com.example.vaadintestgridtree.widgetset.client;

import com.example.vaadintestgridtree.CellWrapper;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.vaadin.client.connectors.ClickableRendererConnector;
import com.vaadin.client.renderers.ClickableRenderer.RendererClickHandler;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;

@Connect(com.example.vaadintestgridtree.gridtree.treenoderenderer.TreeNodeExpandButtonRenderer.class)
public class TreeNodeExpandButtonRendererConnector extends
		ClickableRendererConnector<CellWrapper> {

    @Override
    public TreeNodeExpandButtonRenderer getRenderer() {
        return (TreeNodeExpandButtonRenderer) super.getRenderer();
    }
	@Override
	protected HandlerRegistration addClickHandler(
			RendererClickHandler<JsonObject> handler) {
		return getRenderer().addClickHandler(handler);
	}

}
