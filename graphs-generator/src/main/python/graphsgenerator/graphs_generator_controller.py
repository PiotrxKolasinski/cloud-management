from flask import Blueprint, request

import graphsgenerator.graphs_generator as gg

graphsGeneratorRoute = Blueprint('graphsGenerator', 'graphsGenerator', template_folder='generator')


@graphsGeneratorRoute.route('/generate', methods=['POST'])
def generateGraphs():
    data = request.get_json(force=True)
    gg.generate(data)
    return ""
