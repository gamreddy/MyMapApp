package demoApp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.LegendInfo;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;

public class MyMapApp extends Application {

	private MapView mapView;
	public static final String IMAGERY_BASEMAP_URL = "http://services.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer";
	public static final String ARCGIS_SERVICE_URL = "https://sampleserver6.arcgisonline.com/arcgis/rest/services/Census/MapServer";

	@Override
	public void start(Stage stage) throws Exception {

		// create stack pane and application scene
		StackPane stackPane = new StackPane();
		Scene scene = new Scene(stackPane);
		// set title, size, and add scene to stage
		stage.setTitle("Display Map Sample");
		stage.setWidth(800);
		stage.setHeight(700);
		stage.setScene(scene);
		stage.show();

		final ArcGISMapImageLayer imageLayer = new ArcGISMapImageLayer(ARCGIS_SERVICE_URL);

		// create a ArcGISMap with the a Basemap instance with an Imagery base
		// layer
		ArcGISMap map = new ArcGISMap(Basemap.createImagery());
		// add layer to ArcGISMap's layer list
		map.getOperationalLayers().add(imageLayer);

		// set the map to be displayed in this view
		mapView = new MapView();
		mapView.setMap(map);

		// add the map view to stack pane
		stackPane.getChildren().addAll(mapView);

		// try to retrieve legendinfo on the imageLayer
		getLegendInfo(imageLayer);

	}

	private void getLegendInfo(final ArcGISMapImageLayer imageLayer) {
		imageLayer.addDoneLoadingListener(() -> {

			ListenableFuture<List<LegendInfo>> future = imageLayer.fetchLegendInfosAsync();

			future.addDoneListener(() -> {
				try {
					List<LegendInfo> infos = future.get();
					// check if anything is returned
					System.out.println(infos.size());
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Legend Infos Retrieved...");
					alert.setHeaderText(imageLayer.getName());
					alert.setContentText("No of infos: " + infos.size());
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}

			});

		});
	}

	/**
	 * Stops and releases all resources used in application.
	 */
	@Override
	public void stop() throws Exception {
		if (mapView != null) {
			mapView.dispose();
		}
	}

	/**
	 * Opens and runs application.
	 *
	 * @param args
	 *            arguments passed to this application
	 */
	public static void main(String[] args) {
		// Note: only use this if you need to work with a proxy in ur office
		// MyProxyUtil.setupProxy();
		Application.launch(args);
	}
}
