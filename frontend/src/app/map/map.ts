// export class EasyMap {
//     mapContainer:any;
//     allMarkers: any;
//     allCircles: any;
//     posX: any;
//     posY: any;
//     map: any;

//     constructor(posX=48.8534100,posY=2.34,zoom= 10, MAP_CONTAINER_IDNAME="mapContainer") {

//         this.mapContainer=document.getElementById(MAP_CONTAINER_IDNAME);
  
//         this.allMarkers=[];
//         this.allCircles=[];
//         this.posX=posX;
//         this.posY=posY;
        
//         var mapboxTiles = L.tileLayer('https://{s}.tile.openstreetmap.de/{z}/{x}/{y}.png', {
//                           attribution: '<a href="http://www.mapbox.com/about/maps/" target="_blank">Mate</a>' });
//         this.map = L.map(this.mapContainer).addLayer(mapboxTiles).setView([posX, posY],zoom); // lat, long et "zoom"

//     }
  
//     // Getters
//     getAllMarkers(){
//       return this.allMarkers;
//     }
  
//     getAllCircles(){
//       return this.allCircles;
//     }
  
//     getMap(){
//       return this.map;
//     }
  
//     createMarker(posX: any, posY: any, markerIcon: any = "/assets/localisationIcon.png", redirection=null){
//       var marker = L.marker([posX,posY]);
//       if (markerIcon!=null){
//         marker.setIcon(this.createIcon(markerIcon));
//       }
//       if (redirection!=null){
//         marker.on('click', function() {
//           window.location.href = redirection;
//         });
//       }
//       this.allMarkers.push(marker);
//       return marker;
//     }
  
//     displayMarker(posX: any,posY: any,markerIcon= null){
//       var marker= this.createMarker(posX,posY,markerIcon);
//       marker.addTo(this.map);
//       return marker;
//     }
  
//     removeMarker(marker: any){
//       this.map.removeLayer(marker);
  
//     }
    
//     displayLabelOnMarker(marker: any, text: any){
//       marker.bindPopup(text)
//       // rajouter '.openPopup();' pour l'ouvrir toujours et pas seulement au click
//     }
  
//     drawPathBetweenPoints(points: any) {
//       const coordinates = points.map( (point:any) => [point.lat, point.lng]);
//       const polyline = L.polyline(coordinates).addTo(this.map);
//       return polyline
//     }
// }    