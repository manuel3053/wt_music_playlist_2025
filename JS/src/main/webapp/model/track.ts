export class Track {
  id!: number;
  loaderId!: number;
  filePath!: string;
  imagePath!: string;
  title!: string;
  author!: string;
  albumTitle!: string;
  albumPublicationYear!: number;
  genre!: string;
  position?: number;
}
