const { Router } = require("express");
const articulosController = require('../controllers/articulos.controller');

const router = Router();

router.get('/',articulosController.obtenerArticulos); 
router.get('/:cb',articulosController.obtenerArticulo);
router.post('/insert',articulosController.insertarArticulo);
router.put('/update/:cb',articulosController.actualizarArticulo);
router.delete('/delete/:cb',articulosController.eliminarArticulo);
module.exports=router; 