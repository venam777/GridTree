package org.vaadin.gridtree;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;
import org.vaadin.gridtree.client.CellWrapper;
import org.vaadin.gridtree.treenoderenderer.TreeNodeExpandButtonRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GridTree extends Grid {

    public interface TreeGridContainer extends Container.Hierarchical, Indexed {

        boolean isItemExpanded(Object itemId);

        int getLevel(Object itemId);

        void setExpanded(Object itemId, boolean expand);
    }

	Column expandedColumn;
	protected TreeGridContainer container;
	//private final HashMap<Object, CellWrapper> itemIdToWrappers=new HashMap<Object, CellWrapper>();
	private final  String expandColumnPropertyId ;
	/**
	 *
	 * @param container
	 * @param expandColumnPropertyId - propertyId of the container which will have an expand button
	 */
	public GridTree(TreeGridContainer container,String expandColumnPropertyId) {
		super();
		this.expandColumnPropertyId=expandColumnPropertyId;
		this.container= container;
		//fillContainerWithCellWrappers();
		super.setContainerDataSource(this.container);
		//expandedColumn=getColumn(expandColumnPropertyId);
        expandedColumn = getColumn("id");
		addExpandColumnRenderer(expandedColumn);
		reorderColumns();
	}

//	private void fillContainerWithCellWrappers() {
//		itemIdToWrappers.clear();
//		for(final Object id:container.getItemIds()) {
//			itemIdToWrappers.put(id, getWrapperForItem(id));
//		}
//		container.removeContainerProperty(expandColumnPropertyId);
//		final CellWrapper defValue=new CellWrapper("", "0", false, false,0);
//		this.container.addContainerProperty(expandColumnPropertyId, CellWrapper.class, defValue);
		/*for(final Entry<Object, CellWrapper> pair:itemIdToWrappers.entrySet()){
			container.getItem(pair.getKey()).getItemProperty(expandColumnPropertyId).setValue(pair.getValue());
		};*/
//	}

    protected CellWrapper getWrapperForItem(Object itemId) {
        Object propValue = container.getContainerProperty(itemId, expandColumnPropertyId).getValue();
        final String value = propValue != null ? propValue.toString() : "";
        final Boolean hasChildren=container.hasChildren(itemId);
        final Boolean isExpanded=container.isItemExpanded(itemId);
        final Integer level=container.getLevel(itemId);
        return new CellWrapper(value, itemId, hasChildren, isExpanded,level);
    }

	@Override
	/**
	 * container has to contain expandColumnPropertyId
	 * which was specified in the constructor
	 */
	public void setContainerDataSource(Indexed container) {
		this.container= (TreeGridContainer) container;
//		fillContainerWithCellWrappers();
		super.setContainerDataSource(container);
	}

	private void reorderColumns() {
		final List<Object> propertyIds=new ArrayList<Object>();
		propertyIds.add(expandedColumn.getPropertyId());
		for(final Object propId:container.getContainerPropertyIds()) {
			if(!propId.equals(expandedColumn.getPropertyId())) {
				propertyIds.add(propId);
			}
		};
		setColumnOrder(propertyIds.toArray());
	}

	private void addExpandColumnRenderer(final Column column) {
        final Converter converter = new Converter<CellWrapper, Long>() {
            @Override
            public Long convertToModel(CellWrapper cellWrapper, Class<? extends Long> aClass, Locale locale) throws ConversionException {
                return (Long) cellWrapper.getItemId();
            }

            @Override
            public CellWrapper convertToPresentation(Long itemId, Class<? extends CellWrapper> aClass, Locale locale) throws ConversionException {
//                return itemIdToWrappers.get(itemId);
                //return new CellWrapper(container.getContainerProperty(itemId).getValue().toString(), itemId, container.hasChildren(itemId),);
                return getWrapperForItem(itemId);
            }

            @Override
            public Class<Long> getModelType() {
                return Long.class;
            }

            @Override
            public Class<CellWrapper> getPresentationType() {
                return CellWrapper.class;
            }
        };
		final TreeNodeExpandButtonRenderer renderer=new TreeNodeExpandButtonRenderer(CellWrapper.class);
		renderer.addClickListener(new RendererClickListener() {
			@Override
			public void click(RendererClickEvent event) {
                container.setExpanded(event.getItemId(), !container.isItemExpanded(event.getItemId()));
			}
		});
        column.setRenderer(renderer, converter);
	}


}
