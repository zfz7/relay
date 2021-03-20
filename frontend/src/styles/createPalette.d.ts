import * as createPalette from '@material-ui/core'

declare module '@material-ui/core/styles/createPalette' {
  interface PaletteOptions {
    darkBlue?: PaletteColorOptions
    lightBlue?: PaletteColorOptions
    cyan?: PaletteColorOptions
    green?: PaletteColorOptions
    orange?: PaletteColorOptions
    yellow?: PaletteColorOptions
    amber?: PaletteColorOptions
    red?: PaletteColorOptions
    purple?: PaletteColorOptions
    pink?: PaletteColorOptions
    brown?: PaletteColorOptions
    maroon?: PaletteColorOptions
    olive?: PaletteColorOptions
    backgroundMedium?: PaletteColorOptions
  }
  interface Palette {
    darkBlue: PaletteColor
    lightBlue: PaletteColor
    cyan: PaletteColor
    green: PaletteColor
    orange: PaletteColor
    yellow: PaletteColor
    amber: PaletteColor
    red: PaletteColor
    purple: PaletteColor
    pink: PaletteColor
    brown: PaletteColor
    maroon: PaletteColor
    olive: PaletteColor
    backgroundMedium: PaletteColor
  }
}
