export interface Component {
  get css(): string
  get template(): string
  buildEventListeners(): void
}


