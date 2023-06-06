import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import * as L from 'leaflet';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnChanges{
  @Input() monuments: any[] = [];
  
  map: L.Map | any;
  markersLayer: L.LayerGroup | any;
  coordinates: any[] = [];

  markerIcon = L.icon({
    iconUrl: '../../assets/marker-icon.png',
    shadowUrl: '../../assets/marker-shadow.png'
  });

  ngOnChanges(changes: SimpleChanges): void {
    if (changes) {
      console.warn(this.monuments);
      this.updateMarkers();
    }
  }

  private initializeMap(): void {
    this.map = L.map('map').setView([48.8566, 2.3522], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors',
      maxZoom: 18
    }).addTo(this.map);

    this.markersLayer = L.layerGroup().addTo(this.map);
  }

  private updateMarkers(): void {
    if (!this.map) {
      this.initializeMap();
    }

    if (this.markersLayer) {
      this.markersLayer.clearLayers();

      for (let item of this.monuments) {
        L.marker(item.coordinates, { icon: this.markerIcon })
          .bindPopup(item.name)
          .addTo(this.markersLayer);
        
        this.coordinates.push(item.coordinates);
      }
      this.map.setView(this.monuments[1].coordinates, 13);
      const polyline = L.polyline(this.coordinates, { color: 'coral'}).addTo(this.map);
    }
  }
}